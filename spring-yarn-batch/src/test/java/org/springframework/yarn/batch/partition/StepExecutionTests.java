package org.springframework.yarn.batch.partition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.StepExecutionSplitter;
import org.springframework.batch.core.step.StepLocator;
import org.springframework.batch.support.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Testing simple concepts of handling step partitioning
 * and serialization for cases where remote node would
 * ask step execution over the channel.
 * 
 * @author Janne Valkealahti
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StepExecutionTests {

    @Autowired
    private ApplicationContext ctx;
    
    @Test
    public void testSomething() throws Exception {
        JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
        Job job = (Job) ctx.getBean("job");
        
        JobExecution run = jobLauncher.run(job, new JobParameters());
        assertThat(run, notNullValue());
    }
    
    
    public static class ExamplePartitionHandler implements PartitionHandler {
        
        StepLocator stepLocator;
        JobExplorer jobExplorer;
        
        @Override
        public Collection<StepExecution> handle(StepExecutionSplitter stepSplitter, StepExecution stepExecution)
                throws Exception {
            Set<StepExecution> executions = stepSplitter.split(stepExecution, 2);
            
            Collection<StepExecution> result = new ArrayList<StepExecution>();
            for(StepExecution execution : executions) {
                
                Long jobExecutionId = stepExecution.getJobExecutionId();
                Long stepExecutionId = stepExecution.getId();
                String stepName = "remoteStep";
                StepExecution stepExecution2 = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
                
                byte[] serialized = SerializationUtils.serialize(stepExecution2);
//                byte[] serialized = SerializationUtils.serialize(execution);
                StepExecution deserialized = (StepExecution) SerializationUtils.deserialize(serialized);
                
//                SerializationUtils.serialize(object)
                
                Step step = stepLocator.getStep("remoteStep");
//                step.execute(execution);               
//                result.add(execution);
                step.execute(deserialized);               
                result.add(deserialized);
            }
            //StepExecution stepExecution = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
            
            return result;
        }

        public void setStepLocator(StepLocator stepLocator) {
            this.stepLocator = stepLocator;
        }

        public void setJobExplorer(JobExplorer jobExplorer) {
            this.jobExplorer = jobExplorer;
        }
        
    }

}
