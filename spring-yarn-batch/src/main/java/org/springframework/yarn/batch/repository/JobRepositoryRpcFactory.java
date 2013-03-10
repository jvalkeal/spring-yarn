package org.springframework.yarn.batch.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.mortbay.log.Log;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameter.ParameterType;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.yarn.am.GenericRpcMessage;
import org.springframework.yarn.am.RpcMessage;
import org.springframework.yarn.batch.repository.bindings.AddStepExecutionsReq;
import org.springframework.yarn.batch.repository.bindings.CreateJobInstanceReq;
import org.springframework.yarn.batch.repository.bindings.ExecutionContextType;
import org.springframework.yarn.batch.repository.bindings.ExecutionContextType.ObjectEntry;
import org.springframework.yarn.batch.repository.bindings.FindJobExecutionsReq;
import org.springframework.yarn.batch.repository.bindings.FindRunningJobExecutionsReq;
import org.springframework.yarn.batch.repository.bindings.GetExecutionContextReq;
import org.springframework.yarn.batch.repository.bindings.GetJobExecutionReq;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceByIdReq;
import org.springframework.yarn.batch.repository.bindings.GetJobInstanceReq;
import org.springframework.yarn.batch.repository.bindings.GetJobInstancesReq;
import org.springframework.yarn.batch.repository.bindings.GetJobNamesReq;
import org.springframework.yarn.batch.repository.bindings.GetLastJobExecutionReq;
import org.springframework.yarn.batch.repository.bindings.GetStepExecutionReq;
import org.springframework.yarn.batch.repository.bindings.JobExecutionType;
import org.springframework.yarn.batch.repository.bindings.JobInstanceType;
import org.springframework.yarn.batch.repository.bindings.JobParameterType;
import org.springframework.yarn.batch.repository.bindings.JobParametersType;
import org.springframework.yarn.batch.repository.bindings.SaveExecutionContextReq;
import org.springframework.yarn.batch.repository.bindings.SaveJobExecutionReq;
import org.springframework.yarn.batch.repository.bindings.SaveStepExecutionReq;
import org.springframework.yarn.batch.repository.bindings.StepExecutionType;
import org.springframework.yarn.batch.repository.bindings.SynchronizeStatusReq;
import org.springframework.yarn.batch.repository.bindings.UpdateExecutionContextReq;
import org.springframework.yarn.batch.repository.bindings.UpdateJobExecutionReq;
import org.springframework.yarn.batch.repository.bindings.UpdateStepExecutionReq;
import org.springframework.yarn.integration.ip.mind.MindRpcMessageHolder;
import org.springframework.yarn.integration.support.JacksonUtils;

/**
 * Helper class providing factory methods for building requests used
 * in remote job repository functionality.
 * 
 * @author Janne Valkealahti
 *
 */
public class JobRepositoryRpcFactory {

    private static ObjectMapper mapper = JacksonUtils.getObjectMapper();
    
    /**
     * Builds a request to save {@link StepExecution}.
     * 
     * @param stepExecution the step execution
     * @return a {@link RpcMessage} wrapping {@link MindRpcMessageHolder}
     * @throws Exception if error occurred
     */
    public static RpcMessage<?> buildSaveStepExecutionReq(StepExecution stepExecution) throws Exception {        
        SaveStepExecutionReq req = new SaveStepExecutionReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "SaveStepExecutionReq");
                
        req.stepExecution = buildStepExecutionType(stepExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    public static RpcMessage<?> buildAddStepExecutionReq(JobExecution jobExecution) throws Exception {        
        AddStepExecutionsReq req = new AddStepExecutionsReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "AddStepExecutionsReq");
                
        req.jobExecution = buildJobExecutionType(jobExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    public static RpcMessage<?> buildUpdateStepExecutionReq(StepExecution stepExecution) throws Exception {        
        UpdateStepExecutionReq req = new UpdateStepExecutionReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "UpdateStepExecutionReq");
                
        req.stepExecution = buildStepExecutionType(stepExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    public static RpcMessage<?> buildGetStepExecutionReq(JobExecution jobExecution, Long stepExecutionId) throws Exception {        
        GetStepExecutionReq req = new GetStepExecutionReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "GetStepExecutionReq");
                
        req.jobExecution = buildJobExecutionType(jobExecution);
        req.stepExecutionId = stepExecutionId;

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    public static RpcMessage<?> buildUpdateJobExecutionReq(JobExecution jobExecution) throws Exception {        
        UpdateJobExecutionReq req = new UpdateJobExecutionReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "UpdateJobExecutionReq");
                
        req.jobExecution = buildJobExecutionType(jobExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    public static RpcMessage<?> buildSynchronizeStatusReq(JobExecution jobExecution) throws Exception {        
        SynchronizeStatusReq req = new SynchronizeStatusReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "SynchronizeStatusReq");
                
        req.jobExecution = buildJobExecutionType(jobExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    
    public static RpcMessage<?> buildSaveJobExecutionReq(JobExecution jobExecution) throws Exception {        
        SaveJobExecutionReq req = new SaveJobExecutionReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "SaveJobExecutionReq");
                
        req.jobExecution = buildJobExecutionType(jobExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    
    public static RpcMessage<?> buildSaveExecutionContextReq(StepExecution stepExecution) throws Exception {        
        SaveExecutionContextReq req = new SaveExecutionContextReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "SaveExecutionContextReq");
                
        req.stepExecution = buildStepExecutionType(stepExecution);
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    public static RpcMessage<?> buildSaveExecutionContextReq(JobExecution jobExecution) throws Exception {        
        SaveExecutionContextReq req = new SaveExecutionContextReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "SaveExecutionContextReq");
                
        req.jobExecution = buildJobExecutionType(jobExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    public static RpcMessage<?> buildUpdateExecutionContextReq(StepExecution stepExecution) throws Exception {        
        UpdateExecutionContextReq req = new UpdateExecutionContextReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "UpdateExecutionContextReq");
                
        req.stepExecution = buildStepExecutionType(stepExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    public static RpcMessage<?> buildUpdateExecutionContextReq(JobExecution jobExecution) throws Exception {        
        UpdateExecutionContextReq req = new UpdateExecutionContextReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "UpdateExecutionContextReq");
                
        req.jobExecution = buildJobExecutionType(jobExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    
    public static RpcMessage<?> buildGetExecutionContextReq(StepExecution stepExecution) throws Exception {        
        GetExecutionContextReq req = new GetExecutionContextReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "GetExecutionContextReq");
                
        req.stepExecution = buildStepExecutionType(stepExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    public static RpcMessage<?> buildGetExecutionContextReq(JobExecution jobExecution) throws Exception {        
        GetExecutionContextReq req = new GetExecutionContextReq();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "GetExecutionContextReq");
                
        req.jobExecution = buildJobExecutionType(jobExecution);

        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    /**
     * Creates {@link StepExecutionType} from {@link StepExecution}.
     * 
     * @param stepExecution the step execution
     * @return the step execution type
     * @see #buildStepExecutionType(StepExecution, JobExecution)
     */
    public static StepExecutionType buildStepExecutionType(StepExecution stepExecution) {
        return buildStepExecutionType(stepExecution, null);
    }
    
    /**
     * Creates {@link StepExecutionType} from {@link StepExecution}. Second
     * argument {@link JobExecution} is only used as a back reference to
     * prevent never ending loop for serialization logic due to references
     * between {@link StepExecution} and {@link JobExecution}.
     * 
     * @param stepExecution the step execution
     * @param jobExecution the job execution
     * @return the step execution type
     */
    public static StepExecutionType buildStepExecutionType(StepExecution stepExecution, JobExecution jobExecution) {
        StepExecutionType type = new StepExecutionType();
        type.id = stepExecution.getId();
        type.version = stepExecution.getVersion();
        type.stepName = stepExecution.getStepName();

        type.jobExecution = buildJobExecutionType(stepExecution.getJobExecution(), stepExecution);
        
        type.status = stepExecution.getStatus();
        type.readCount = stepExecution.getReadCount();
        type.writeCount = stepExecution.getWriteCount();
        type.commitCount = stepExecution.getCommitCount();
        type.rollbackCount = stepExecution.getRollbackCount();
        type.readSkipCount = stepExecution.getReadSkipCount();
        type.processSkipCount = stepExecution.getProcessSkipCount();
        type.writeSkipCount = stepExecution.getWriteSkipCount();
        type.startTime = nullsafeToMillis(stepExecution.getStartTime());
        type.endTime = nullsafeToMillis(stepExecution.getEndTime());
        type.lastUpdated = nullsafeToMillis(stepExecution.getLastUpdated());        
        type.terminateOnly = stepExecution.isTerminateOnly();
        type.filterCount = stepExecution.getFilterCount();        
        type.executionContext = convertExecutionContext(stepExecution.getExecutionContext());
        
        return type;
    }
    
    /**
     * Converts a {@link ExecutionContext} to {@link ExecutionContextType}.
     * 
     * @param executionContext the execution context
     * @return converted execution context type
     */
    public static ExecutionContextType convertExecutionContext(ExecutionContext executionContext) {
        ExecutionContextType type = new ExecutionContextType();        
        type.map = new HashMap<String, ExecutionContextType.ObjectEntry>();        
        for(Entry<String, Object> entry : executionContext.entrySet()) {
            Object value = entry.getValue();
            type.map.put(entry.getKey(), new ExecutionContextType.ObjectEntry(value, value.getClass().getCanonicalName()));
        }        
        return type;
    }

    /**
     * Converts a {@link ExecutionContextType} to {@link ExecutionContext}.
     * 
     * @param executionContextType the execution context type
     * @return converted execution context
     */
    public static ExecutionContext convertExecutionContextType(ExecutionContextType executionContextType) {
        Map<String, Object> map = new ConcurrentHashMap<String, Object>();
        
        for(Entry<String, ObjectEntry> entry : executionContextType.map.entrySet()) {
            String key = entry.getKey();
            ObjectEntry objectEntry = entry.getValue();
            Object value = null;
            if(String.class.getCanonicalName().equals(objectEntry.clazz)) {
                value = objectEntry.obj;
            } else if(Integer.class.getCanonicalName().equals(objectEntry.clazz)) {
                value = objectEntry.obj;
            } else if(Long.class.getCanonicalName().equals(objectEntry.clazz)) {
                if(objectEntry.obj instanceof Integer) {
                    value = new Long((Integer)objectEntry.obj);
                } else {
                    value = objectEntry.obj;                    
                }
            } else if(Double.class.getCanonicalName().equals(objectEntry.clazz)) {
                value = objectEntry.obj;                
            }
            if(value != null) {
                // should we throw error if null?
                map.put(key, value);                
            }
        }
        
        return new ExecutionContext(map);
    }
    
    public static StepExecution convertStepExecutionType(StepExecutionType type) {
        JobExecution jobExecution = convertJobExecutionType(type.jobExecution);
        StepExecution stepExecution = type.id != null ? new StepExecution(type.stepName, jobExecution, type.id)
                : new StepExecution(type.stepName, jobExecution);
        stepExecution.setVersion(type.version);
        stepExecution.setStatus(type.status);
        stepExecution.setReadCount(type.readCount);
        stepExecution.setWriteCount(type.writeCount);
        stepExecution.setCommitCount(type.commitCount);
        stepExecution.setRollbackCount(type.rollbackCount);
        stepExecution.setReadSkipCount(type.readSkipCount);
        stepExecution.setProcessSkipCount(type.processSkipCount);
        stepExecution.setWriteSkipCount(type.writeSkipCount);

        stepExecution.setStartTime(nullsafeToDate(type.startTime));
        stepExecution.setEndTime(nullsafeToDate(type.endTime));
        stepExecution.setLastUpdated(nullsafeToDate(type.lastUpdated));
        
        if(type.terminateOnly.booleanValue()) {
            stepExecution.setTerminateOnly();            
        }
        stepExecution.setFilterCount(type.filterCount);
        
        ExecutionContext executionContext = convertExecutionContextType(type.executionContext);
        stepExecution.setExecutionContext(executionContext);
        
        return stepExecution;
    }

    /**
     * Creates {@link JobExecutionType} from {@link JobExecution}.
     * 
     * @param jobExecution the job execution
     * @return the job execution type
     * @see #buildJobExecutionType(JobExecution, StepExecution)
     */
    public static JobExecutionType buildJobExecutionType(JobExecution jobExecution) {
        return buildJobExecutionType(jobExecution, null);
    }
    
    /**
     * Creates {@link JobExecutionType} from {@link JobExecution}. Second
     * argument {@link StepExecution} is only used as a back reference to
     * prevent never ending loop for serialization logic due to references
     * between {@link StepExecution} and {@link JobExecution}.
     * 
     * @param jobExecution the job execution
     * @param stepExecution the step execution
     * @return the job execution type
     */
    public static JobExecutionType buildJobExecutionType(JobExecution jobExecution, StepExecution stepExecution) {
        JobExecutionType type = new JobExecutionType();
        type.id = jobExecution.getId();
        type.version = jobExecution.getVersion();
        
        type.jobInstance = buildJobInstanceType(jobExecution.getJobInstance());            
        
        type.status = jobExecution.getStatus();
        type.startTime = nullsafeToMillis(jobExecution.getStartTime());
        type.endTime = nullsafeToMillis(jobExecution.getEndTime());
        type.createTime = nullsafeToMillis(jobExecution.getCreateTime());
        type.lastUpdated = nullsafeToMillis(jobExecution.getLastUpdated());
        type.exitStatus = jobExecution.getExitStatus().getExitCode();
        
        type.stepExecutions = new ArrayList<StepExecutionType>();
        
        for (StepExecution stepExecution2 : jobExecution.getStepExecutions()) {
            if(!jobExecution.getStepExecutions().contains(stepExecution2) || stepExecution == null) {
                type.stepExecutions.add(buildStepExecutionType(stepExecution2, jobExecution));                
            }
        }
        
        type.executionContext = convertExecutionContext(jobExecution.getExecutionContext());
        
        return type;
    }

    public static JobExecution convertJobExecutionType(JobExecutionType type) {
        JobInstance jobInstance = convertJobInstanceType(type.jobInstance);
        JobExecution jobExecution = new JobExecution(jobInstance, type.id);
        jobExecution.setVersion(type.version);
        jobExecution.setStatus(type.status);
        jobExecution.setStartTime(nullsafeToDate(type.startTime));
        jobExecution.setEndTime(nullsafeToDate(type.endTime));
        jobExecution.setCreateTime(nullsafeToDate(type.createTime));
        jobExecution.setLastUpdated(nullsafeToDate(type.lastUpdated));
        jobExecution.setExitStatus(new ExitStatus(type.exitStatus));

        List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
        for(StepExecutionType stepExecutionType : type.stepExecutions) {
            StepExecution convertStepExecutionType = convertStepExecutionType(stepExecutionType);
            stepExecutions.add(convertStepExecutionType);
        }
        jobExecution.addStepExecutions(stepExecutions);
        
        ExecutionContext executionContext = convertExecutionContextType(type.executionContext);
        
        jobExecution.setExecutionContext(executionContext);
        
        return jobExecution;
    }
    
    public static JobInstanceType buildJobInstanceType(JobInstance jobInstance) {
        JobInstanceType type = new JobInstanceType();
        type.id = jobInstance.getId();
        type.version = jobInstance.getVersion();
        type.jobParameters = convertJobParameters(jobInstance.getJobParameters());
        type.jobName = jobInstance.getJobName();
        return type;
    }
    
    public static JobInstance convertJobInstanceType(JobInstanceType type) {
        JobParameters jobParameters = convertJobParametersType(type.jobParameters);
        JobInstance jobInstance = new JobInstance(type.id, jobParameters, type.jobName);
        jobInstance.setVersion(type.version);
        return jobInstance;
    }
    
    public static JobParametersType convertJobParameters(JobParameters jobParameters) {
        JobParametersType type = new JobParametersType();
        type.parameters = new HashMap<String, JobParameterType>();
        
        Map<String, JobParameter> parameters = jobParameters.getParameters();
        for(Entry<String, JobParameter> entry : parameters.entrySet()) {
            JobParameterType jobParameterType = new JobParameterType();
            jobParameterType.parameter = entry.getValue().getValue();
            jobParameterType.parameterType = entry.getValue().getType();
            type.parameters.put(entry.getKey(), jobParameterType);
        }
        
        return type;
    }
    
    public static JobParameters convertJobParametersType(JobParametersType type) {
        
        Map<String, JobParameter> map = new HashMap<String, JobParameter>();        
        for(Entry<String, JobParameterType> entry : type.parameters.entrySet()) {
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
        return new JobParameters(map);        
    }
    
    public static RpcMessage<?> buildCreateJobInstanceReq(String jobName, JobParameters jobParameters) throws Exception {        
        CreateJobInstanceReq req = new CreateJobInstanceReq();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "CreateJobInstanceReq");
        
        Map<String, JobParameterType> map = new HashMap<String, JobParameterType>();
        for(Entry<String, JobParameter> parameter : jobParameters.getParameters().entrySet()) {
            JobParameterType type = new JobParameterType();
            type.parameter = parameter.getValue().getValue();
            type.parameterType = parameter.getValue().getType();
            map.put(parameter.getKey(), type);
        }
        
        req.jobName = jobName;
        req.jobParameters = map;
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    public static RpcMessage<?> buildGetJobInstanceReq(String jobName, JobParameters jobParameters) throws Exception {        
        GetJobInstanceReq req = new GetJobInstanceReq();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "GetJobInstanceReq");
        
        Map<String, JobParameterType> map = new HashMap<String, JobParameterType>();
        for(Entry<String, JobParameter> parameter : jobParameters.getParameters().entrySet()) {
            JobParameterType type = new JobParameterType();
            type.parameter = parameter.getValue().getValue();
            type.parameterType = parameter.getValue().getType();
            map.put(parameter.getKey(), type);
        }
        
        req.jobName = jobName;
        req.jobParameters = map;
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    public static RpcMessage<?> buildGetJobInstanceByIdReq(Long id) throws Exception {        
        GetJobInstanceByIdReq req = new GetJobInstanceByIdReq();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "GetJobInstanceByIdReq");
        
        req.id = id;
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    public static RpcMessage<?> buildGetJobInstancesReq(String jobName, int start, int count) throws Exception {        
        GetJobInstancesReq req = new GetJobInstancesReq();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "GetJobInstancesReq");
        
        req.jobName = jobName;
        req.count = count;
        req.start = start;
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }


    public static RpcMessage<?> buildGetLastJobExecution(JobInstance jobInstance) throws Exception {        
        GetLastJobExecutionReq req = new GetLastJobExecutionReq();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "GetLastJobExecutionReq");
        
        req.jobInstance = buildJobInstanceType(jobInstance);
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    public static RpcMessage<?> buildFindJobExecutionsReq(JobInstance jobInstance) throws Exception {        
        FindJobExecutionsReq req = new FindJobExecutionsReq();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "FindJobExecutionsReq");
        
        req.jobInstance = buildJobInstanceType(jobInstance);
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }

    public static RpcMessage<?> buildFindRunningJobExecutionsReq(String jobName) throws Exception {        
        FindRunningJobExecutionsReq req = new FindRunningJobExecutionsReq();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "FindRunningJobExecutionsReq");
        
        req.jobName = jobName;
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    public static RpcMessage<?> buildGetJobExecutionReq(Long executionId) throws Exception {        
        GetJobExecutionReq req = new GetJobExecutionReq();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "GetJobExecutionReq");
        
        req.executionId = executionId;
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    public static RpcMessage<?> buildGetJobNamesReq() throws Exception {        
        GetJobNamesReq req = new GetJobNamesReq();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("type", "GetJobNamesReq");
        
        MindRpcMessageHolder holder = new MindRpcMessageHolder(headers, mapper.writeValueAsString(req));
        RpcMessage<MindRpcMessageHolder> message = new GenericRpcMessage<MindRpcMessageHolder>(holder);
        return message;
    }
    
    public static <T> T convert(MindRpcMessageHolder holder, Class<T> clazz) throws Exception {
        return mapper.readValue(holder.getContent(), clazz);
    }

    @SuppressWarnings("rawtypes")
    public static <T> T convert(MindRpcMessageHolder holder, TypeReference valueTypeRef) throws Exception {
        return mapper.readValue(holder.getContent(), valueTypeRef);
    }
    
    private static Long nullsafeToMillis(Date date) {
        if(date != null) {
            return date.getTime();
        } else {
            return null;
        }
    }

    private static Date nullsafeToDate(Long millis) {
        if(millis != null) {
            return new Date(millis);
        } else {
            return null;
        }
    }
    
}
