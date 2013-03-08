package org.springframework.yarn.batch.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameter.ParameterType;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.yarn.am.GenericRpcMessage;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.batch.repository.bindings.AddStepExecutionsReq;
import org.springframework.yarn.batch.repository.bindings.AddStepExecutionsRes;
import org.springframework.yarn.batch.repository.bindings.CreateJobInstanceReq;
import org.springframework.yarn.batch.repository.bindings.ExecutionContextType;
import org.springframework.yarn.batch.repository.bindings.FindJobExecutionsReq;
import org.springframework.yarn.batch.repository.bindings.FindJobExecutionsRes;
import org.springframework.yarn.batch.repository.bindings.FindRunningJobExecutionsReq;
import org.springframework.yarn.batch.repository.bindings.FindRunningJobExecutionsRes;
import org.springframework.yarn.batch.repository.bindings.GetExecutionContextReq;
import org.springframework.yarn.batch.repository.bindings.GetExecutionContextRes;
import org.springframework.yarn.batch.repository.bindings.GetJobExecutionReq;
import org.springframework.yarn.batch.repository.bindings.GetJobExecutionRes;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceByIdReq;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceByIdRes;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceReq;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceRes;
import org.springframework.yarn.batch.repository.bindings.GetJobInstancesReq;
import org.springframework.yarn.batch.repository.bindings.GetLastJobExecutionReq;
import org.springframework.yarn.batch.repository.bindings.GetLastJobExecutionRes;
import org.springframework.yarn.batch.repository.bindings.GetStepExecutionReq;
import org.springframework.yarn.batch.repository.bindings.GetStepExecutionRes;
import org.springframework.yarn.batch.repository.bindings.JobExecutionType;
import org.springframework.yarn.batch.repository.bindings.JobInstanceType;
import org.springframework.yarn.batch.repository.bindings.JobParameterType;
import org.springframework.yarn.batch.repository.bindings.SaveExecutionContextReq;
import org.springframework.yarn.batch.repository.bindings.SaveExecutionContextRes;
import org.springframework.yarn.batch.repository.bindings.SaveJobExecutionReq;
import org.springframework.yarn.batch.repository.bindings.SaveJobExecutionRes;
import org.springframework.yarn.batch.repository.bindings.SaveStepExecutionReq;
import org.springframework.yarn.batch.repository.bindings.SaveStepExecutionRes;
import org.springframework.yarn.batch.repository.bindings.SynchronizeStatusReq;
import org.springframework.yarn.batch.repository.bindings.SynchronizeStatusRes;
import org.springframework.yarn.batch.repository.bindings.UpdateExecutionContextReq;
import org.springframework.yarn.batch.repository.bindings.UpdateExecutionContextRes;
import org.springframework.yarn.batch.repository.bindings.UpdateJobExecutionReq;
import org.springframework.yarn.batch.repository.bindings.UpdateJobExecutionRes;
import org.springframework.yarn.batch.repository.bindings.UpdateStepExecutionReq;
import org.springframework.yarn.batch.repository.bindings.UpdateStepExecutionRes;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;
import org.springframework.yarn.integration.support.JacksonUtils;

public class JobRepositoryRemoteService implements InitializingBean {

    private final static Log log = LogFactory.getLog(JobRepositoryRemoteService.class);
    private static ObjectMapper mapper = JacksonUtils.getObjectMapper();
    
    private JobExecutionDao jobExecutionDao;
    private JobInstanceDao jobInstanceDao;
    private StepExecutionDao stepExecutionDao;
    private ExecutionContextDao executionContextDao;
    
    private MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mapJobRepositoryFactoryBean, "MapJobRepositoryFactoryBean must not be null");
        setJobExecutionDao(mapJobRepositoryFactoryBean.getJobExecutionDao());
        setJobInstanceDao(mapJobRepositoryFactoryBean.getJobInstanceDao());
        setStepExecutionDao(mapJobRepositoryFactoryBean.getStepExecutionDao());
        setExecutionContextDao(mapJobRepositoryFactoryBean.getExecutionContextDao());
    }

    public void setMapJobRepositoryFactoryBean(MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean) {
        this.mapJobRepositoryFactoryBean = mapJobRepositoryFactoryBean;
    }

    public void setJobExecutionDao(JobExecutionDao jobExecutionDao) {
        this.jobExecutionDao = jobExecutionDao;
    }

    public void setJobInstanceDao(JobInstanceDao jobInstanceDao) {
        this.jobInstanceDao = jobInstanceDao;
    }

    public void setStepExecutionDao(StepExecutionDao stepExecutionDao) {
        this.stepExecutionDao = stepExecutionDao;
    }

    public void setExecutionContextDao(ExecutionContextDao executionContextDao) {
        this.executionContextDao = executionContextDao;
    }
    
    public RpcMessage<?> get(RpcMessage<?> message) {
        MindRpcMessageHolder inHolder = (MindRpcMessageHolder) message.getBody();
        MindRpcMessageHolder outHolder = null;

        String type = inHolder.getHeaders().get("type").trim();
        
        if(log.isDebugEnabled()) {
            log.debug("Handling rpc request for type=" + type);
        }
        
        if (type.equals("CreateJobInstanceReq")) {
            outHolder = handleCreateJobInstance(inHolder);
        } else if (type.equals("GetJobInstanceReq")) {
            outHolder = handleGetJobInstance(inHolder);
        } else if (type.equals("GetJobInstanceByIdReq")) {
            outHolder = handleGetJobInstanceById(inHolder);
        } else if (type.equals("GetJobNamesReq")) {
            outHolder = handleGetJobNames(inHolder);
        } else if (type.equals("GetJobInstancesReq")) {
            outHolder = handleGetJobInstances(inHolder);
        } else if (type.equals("SaveStepExecutionReq")) {
            outHolder = handleSaveStepExecution(inHolder);
        } else if (type.equals("AddStepExecutionsReq")) {
            outHolder = handleAddStepExecutions(inHolder);
        } else if (type.equals("UpdateStepExecutionReq")) {
            outHolder = handleUpdateStepExecution(inHolder);
        } else if (type.equals("GetStepExecutionReq")) {
            outHolder = handleGetStepExecution(inHolder);
        } else if (type.equals("SaveJobExecutionReq")) {
            outHolder = handleSaveJobExecution(inHolder);
        } else if (type.equals("SaveExecutionContextReq")) {
            outHolder = handleSaveExecutionContext(inHolder);
        } else if (type.equals("UpdateExecutionContextReq")) {
            outHolder = handleUpdateExecutionContext(inHolder);
        } else if (type.equals("FindJobExecutionsReq")) {
            outHolder = handleFindJobExecutions(inHolder);
        } else if (type.equals("FindRunningJobExecutionsReq")) {
            outHolder = handleFindRunningJobExecutions(inHolder);
        } else if (type.equals("GetJobExecutionReq")) {
            outHolder = handleGetJobExecution(inHolder);
        } else if (type.equals("GetLastJobExecutionReq")) {
            outHolder = handleGetLastJobExecution(inHolder);
        } else if (type.equals("UpdateJobExecutionReq")) {
            outHolder = handleUpdateJobExecution(inHolder);
        } else if (type.equals("SynchronizeStatusReq")) {
            outHolder = handleSynchronizeStatus(inHolder);
        } else if (type.equals("GetExecutionContextReq")) {
            outHolder = handleGetExecutionContext(inHolder);
        }
        
        if(outHolder == null) {
            throw new RuntimeException("Error finding defined rpc request type");
        }
        
        if(log.isDebugEnabled()) {
            log.debug("Handled rpc request for type=" + type + ". Returning holder " + outHolder);
        }
        
        return new GenericRpcMessage<MindRpcMessageHolder>(outHolder);
    }

    /**
     * handleCreateJobInstance
     */
    private MindRpcMessageHolder handleCreateJobInstance(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;
        try {
            CreateJobInstanceReq request = JobRepositoryRpcFactory.convert(holder, CreateJobInstanceReq.class);
            String jobName = request.jobName;
            
            Map<String, JobParameter> map = new HashMap<String, JobParameter>();
            
            for(Entry<String, JobParameterType> entry : request.jobParameters.entrySet()) {
                ParameterType parameterType = entry.getValue().parameterType;
                if(parameterType == ParameterType.DATE) {
                    map.put(entry.getKey(), new JobParameter(new Date((Integer)entry.getValue().parameter)));
                } else if(parameterType == ParameterType.DOUBLE) {
                    map.put(entry.getKey(), new JobParameter((Double)entry.getValue().parameter));
                } else if(parameterType == ParameterType.LONG) {
                    if(entry.getValue().parameter instanceof Long) {
                        map.put(entry.getKey(), new JobParameter((Long)entry.getValue().parameter));                        
                    } else if(entry.getValue().parameter instanceof Integer) {
                        Long tmp = new Long((Integer)entry.getValue().parameter);
                        map.put(entry.getKey(), new JobParameter(tmp));                                                
                    }
                } else if(parameterType == ParameterType.STRING) {
                    map.put(entry.getKey(), new JobParameter((String)entry.getValue().parameter));                    
                }
            }
            
            JobParameters jobParameters = new JobParameters(map);
            JobInstance createJobInstance = jobInstanceDao.createJobInstance(jobName, jobParameters);
            
            JobInstanceType buildJobInstanceType = JobRepositoryRpcFactory.buildJobInstanceType(createJobInstance);
            byte[] writeValueAsBytes = mapper.writeValueAsBytes(buildJobInstanceType);            
            outHolder = new MindRpcMessageHolder(null, writeValueAsBytes);
            
        } catch (Exception e) {
            log.error("error handling command", e);
        }
        return outHolder;
    }

    /**
     * handleGetJobInstance
     */
    private MindRpcMessageHolder handleGetJobInstance(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;
        try {
            GetJobInstanceReq request = JobRepositoryRpcFactory.convert(holder, GetJobInstanceReq.class);
            String jobName = request.jobName;
            
            Map<String, JobParameter> map = new HashMap<String, JobParameter>();
            
            for(Entry<String, JobParameterType> entry : request.jobParameters.entrySet()) {
                ParameterType parameterType = entry.getValue().parameterType;
                if(parameterType == ParameterType.DATE) {
                    map.put(entry.getKey(), new JobParameter(new Date((Integer)entry.getValue().parameter)));
                } else if(parameterType == ParameterType.DOUBLE) {
                    map.put(entry.getKey(), new JobParameter((Double)entry.getValue().parameter));
                } else if(parameterType == ParameterType.LONG) {
                    map.put(entry.getKey(), new JobParameter((Long)entry.getValue().parameter));
                } else if(parameterType == ParameterType.STRING) {
                    map.put(entry.getKey(), new JobParameter((String)entry.getValue().parameter));                    
                }
            }
            
            JobParameters jobParameters = new JobParameters(map);
            JobInstance jobInstance = jobInstanceDao.getJobInstance(jobName, jobParameters);

            GetJobInstanceRes response = new GetJobInstanceRes();
            if(jobInstance != null) {
                response.jobInstance = JobRepositoryRpcFactory.buildJobInstanceType(jobInstance);
            }
            
            byte[] writeValueAsBytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, writeValueAsBytes);
        } catch (Exception e) {
            log.error("error handling command", e);
        }
        return outHolder;

    }

    /**
     * handleGetJobInstanceById
     */
    private MindRpcMessageHolder handleGetJobInstanceById(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;
        try {
            GetJobInstanceByIdReq request = JobRepositoryRpcFactory.convert(holder, GetJobInstanceByIdReq.class);
            Long id = request.id;
            JobInstance jobInstance = jobInstanceDao.getJobInstance(id);
            GetJobInstanceByIdRes response = new GetJobInstanceByIdRes();
            if(jobInstance != null) {
                response.jobInstance = JobRepositoryRpcFactory.buildJobInstanceType(jobInstance);
            }
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }

    /**
     * handleGetJobNames
     */
    private MindRpcMessageHolder handleGetJobNames(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;
        try {
            List<String> jobNames = jobInstanceDao.getJobNames();
            byte[] writeValueAsBytes = mapper.writeValueAsBytes(jobNames);            
            outHolder = new MindRpcMessageHolder(null, writeValueAsBytes);
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }

    /**
     * handleGetJobInstances
     */
    private MindRpcMessageHolder handleGetJobInstances(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;
        try {
            GetJobInstancesReq request = JobRepositoryRpcFactory.convert(holder, GetJobInstancesReq.class);
            String jobName = request.jobName;
            Integer start = request.start;
            Integer count = request.count;
            List<JobInstance> jobInstances = jobInstanceDao.getJobInstances(jobName, start, count);
            
            List<JobInstanceType> jobInstanceTypes = new ArrayList<JobInstanceType>();
            for(JobInstance jobInstance : jobInstances) {
                jobInstanceTypes.add(JobRepositoryRpcFactory.buildJobInstanceType(jobInstance));
            }
            byte[] writeValueAsBytes = mapper.writeValueAsBytes(jobInstanceTypes);            
            outHolder = new MindRpcMessageHolder(null, writeValueAsBytes);
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }
    
    /**
     * handleSaveExecutionContext
     */
    private MindRpcMessageHolder handleSaveExecutionContext(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            SaveExecutionContextReq request = JobRepositoryRpcFactory.convert(holder, SaveExecutionContextReq.class);

            if(request.stepExecution != null) {
                StepExecution stepExecution = JobRepositoryRpcFactory.convertStepExecutionType(request.stepExecution);
                executionContextDao.saveExecutionContext(stepExecution);                
            } else if(request.jobExecution != null) {
                JobExecution jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(request.jobExecution);
                executionContextDao.saveExecutionContext(jobExecution);                                
            }
            
            SaveExecutionContextRes response = new SaveExecutionContextRes();            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }
    
    /**
     * handleGetExecutionContext
     */
    private MindRpcMessageHolder handleGetExecutionContext(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        
        try {
            GetExecutionContextReq request = JobRepositoryRpcFactory.convert(holder, GetExecutionContextReq.class);
            
            ExecutionContext executionContext = null;
            if(request.stepExecution != null) {
                StepExecution stepExecution = JobRepositoryRpcFactory.convertStepExecutionType(request.stepExecution);                
                executionContext = executionContextDao.getExecutionContext(stepExecution);
            } else if(request.jobExecution != null) {
                JobExecution jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(request.jobExecution);                
                executionContext = executionContextDao.getExecutionContext(jobExecution);
            }
                        
            GetExecutionContextRes response = new GetExecutionContextRes();
            
            response.executionContext = new ExecutionContextType();
            Map<String, Object> map = new HashMap<String, Object>();
            for(Entry<String, Object> entry : executionContext.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }        
            response.executionContext.map = map;
            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }
    
    /**
     * handleUpdateExecutionContext
     */
    private MindRpcMessageHolder handleUpdateExecutionContext(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            UpdateExecutionContextReq request = JobRepositoryRpcFactory.convert(holder, UpdateExecutionContextReq.class);

            if(request.stepExecution != null) {
                StepExecution stepExecution = JobRepositoryRpcFactory.convertStepExecutionType(request.stepExecution);
                executionContextDao.saveExecutionContext(stepExecution);                
            } else if(request.jobExecution != null) {
                JobExecution jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(request.jobExecution);
                executionContextDao.saveExecutionContext(jobExecution);                                
            }
                        
            UpdateExecutionContextRes response = new UpdateExecutionContextRes();            
            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }
    
    /**
     * handleFindJobExecutions
     */
    private MindRpcMessageHolder handleFindJobExecutions(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            FindJobExecutionsReq request = JobRepositoryRpcFactory.convert(holder, FindJobExecutionsReq.class);
            JobInstance jobInstance = JobRepositoryRpcFactory.convertJobInstanceType(request.jobInstance);
            List<JobExecution> jobExecutions = jobExecutionDao.findJobExecutions(jobInstance);
            
            FindJobExecutionsRes response = new FindJobExecutionsRes();            
            response.jobExecutions = new ArrayList<JobExecutionType>();
            for(JobExecution jobExecution : jobExecutions) {
                response.jobExecutions.add(JobRepositoryRpcFactory.buildJobExecutionType(jobExecution));
            }
            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }    

    /**
     * handleSaveJobExecution
     */
    private MindRpcMessageHolder handleSaveJobExecution(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            SaveJobExecutionReq request = JobRepositoryRpcFactory.convert(holder, SaveJobExecutionReq.class);            
            JobExecution jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(request.jobExecution);            
            jobExecutionDao.saveJobExecution(jobExecution);            
            SaveJobExecutionRes response = new SaveJobExecutionRes();
            response.id = jobExecution.getId();
            response.version = jobExecution.getVersion();
            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }

    /**
     * handleUpdateJobExecution
     */
    private MindRpcMessageHolder handleUpdateJobExecution(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            UpdateJobExecutionReq request = JobRepositoryRpcFactory.convert(holder, UpdateJobExecutionReq.class);
            JobExecution jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(request.jobExecution);
            jobExecutionDao.updateJobExecution(jobExecution);
            UpdateJobExecutionRes response = new UpdateJobExecutionRes();
            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }

    /**
     * handleGetLastJobExecution
     */
    private MindRpcMessageHolder handleGetLastJobExecution(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            GetLastJobExecutionReq request = JobRepositoryRpcFactory.convert(holder, GetLastJobExecutionReq.class);
            JobInstance jobInstance = JobRepositoryRpcFactory.convertJobInstanceType(request.jobInstance);
            JobExecution jobExecution = jobExecutionDao.getLastJobExecution(jobInstance);
            
            GetLastJobExecutionRes response = new GetLastJobExecutionRes();
            if(jobExecution != null) {
                response.jobExecution = JobRepositoryRpcFactory.buildJobExecutionType(jobExecution);
            }
            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }    

    /**
     * handleFindRunningJobExecutions
     */
    private MindRpcMessageHolder handleFindRunningJobExecutions(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            FindRunningJobExecutionsReq request = JobRepositoryRpcFactory.convert(holder, FindRunningJobExecutionsReq.class);
            Set<JobExecution> jobExecutions = jobExecutionDao.findRunningJobExecutions(request.jobName);
            FindRunningJobExecutionsRes response = new FindRunningJobExecutionsRes();
            response.jobExecutions = new HashSet<JobExecutionType>();
            
            for(JobExecution jobExecution : jobExecutions) {
                response.jobExecutions.add(JobRepositoryRpcFactory.buildJobExecutionType(jobExecution));
            }
            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }    
    
    /**
     * handleGetJobExecution
     */
    private MindRpcMessageHolder handleGetJobExecution(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            GetJobExecutionReq request = JobRepositoryRpcFactory.convert(holder, GetJobExecutionReq.class);
            JobExecution jobExecution = jobExecutionDao.getJobExecution(request.executionId);
            GetJobExecutionRes response = new GetJobExecutionRes();
            if(jobExecution != null) {
                response.jobExecution = JobRepositoryRpcFactory.buildJobExecutionType(jobExecution);                
            }            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }    
    
    /**
     * handleSynchronizeStatus
     */
    private MindRpcMessageHolder handleSynchronizeStatus(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            SynchronizeStatusReq request = JobRepositoryRpcFactory.convert(holder, SynchronizeStatusReq.class);
            JobExecution jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(request.jobExecution);
            jobExecutionDao.synchronizeStatus(jobExecution);
            
            SynchronizeStatusRes response = new SynchronizeStatusRes();
            response.version = jobExecution.getVersion();
            response.status = jobExecution.getStatus();
            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }

    /**
     * handleSaveStepExecution
     */
    private MindRpcMessageHolder handleSaveStepExecution(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            SaveStepExecutionReq request = JobRepositoryRpcFactory.convert(holder, SaveStepExecutionReq.class);
            StepExecution stepExecution = JobRepositoryRpcFactory.convertStepExecutionType(request.stepExecution);
            stepExecutionDao.saveStepExecution(stepExecution);
            SaveStepExecutionRes response = new SaveStepExecutionRes();
            response.id = stepExecution.getId();            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }
    
    /**
     * handleUpdateStepExecution
     */
    private MindRpcMessageHolder handleUpdateStepExecution(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            UpdateStepExecutionReq request = JobRepositoryRpcFactory.convert(holder, UpdateStepExecutionReq.class);
            StepExecution stepExecution = JobRepositoryRpcFactory.convertStepExecutionType(request.stepExecution);
            stepExecutionDao.updateStepExecution(stepExecution);
            UpdateStepExecutionRes response = new UpdateStepExecutionRes();
//            executionRes.id = stepExecution.getId();            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }
    
    /**
     * handleGetStepExecution
     */
    private MindRpcMessageHolder handleGetStepExecution(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            GetStepExecutionReq request = JobRepositoryRpcFactory.convert(holder, GetStepExecutionReq.class);
            
            JobExecution jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(request.jobExecution);
            
            GetStepExecutionRes response = new GetStepExecutionRes();
            if(request.stepExecutionId != null) {
                StepExecution stepExecution = stepExecutionDao.getStepExecution(jobExecution, request.stepExecutionId);  
                if(stepExecution != null) {
                    response.stepExecution = JobRepositoryRpcFactory.buildStepExecutionType(stepExecution);                                    
                }
            }
            
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }
    
    /**
     * handleAddStepExecutions
     */
    private MindRpcMessageHolder handleAddStepExecutions(MindRpcMessageHolder holder) {
        MindRpcMessageHolder outHolder = null;        
        try {
            AddStepExecutionsReq request = JobRepositoryRpcFactory.convert(holder, AddStepExecutionsReq.class);            
            JobExecution jobExecution = JobRepositoryRpcFactory.convertJobExecutionType(request.jobExecution);            
            stepExecutionDao.addStepExecutions(jobExecution);            
            AddStepExecutionsRes response = new AddStepExecutionsRes();
            response.jobExecution = JobRepositoryRpcFactory.buildJobExecutionType(jobExecution);            
            // add job execution back to response and let caller to merge inlined step executions
            byte[] bytes = mapper.writeValueAsBytes(response);            
            outHolder = new MindRpcMessageHolder(null, bytes);            
        } catch (Exception e) {
            log.error("error handling command", e);            
        }
        return outHolder;
    }
    
}
