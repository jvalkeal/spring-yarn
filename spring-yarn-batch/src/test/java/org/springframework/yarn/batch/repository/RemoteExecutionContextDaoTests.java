package org.springframework.yarn.batch.repository;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.item.ExecutionContext;

public class RemoteExecutionContextDaoTests {

    private JobInstanceDao jobInstanceDao;
    private JobExecutionDao jobExecutionDao;
    private StepExecutionDao stepExecutionDao;
    private ExecutionContextDao contextDao;
    private JobExecution jobExecution;
    private StepExecution stepExecution;
    
    @Before
    public void setUp() {
        StubAppmasterScOperations scOperations = new StubAppmasterScOperations();
        jobInstanceDao = new RemoteJobInstanceDao(scOperations);
        jobExecutionDao = new RemoteJobExecutionDao(scOperations);
        stepExecutionDao = new RemoteStepExecutionDao(scOperations);
        contextDao = new RemoteExecutionContextDao(scOperations);

        JobInstance ji = jobInstanceDao.createJobInstance("testJob", new JobParameters());
        jobExecution = new JobExecution(ji);
        jobExecutionDao.saveJobExecution(jobExecution);
        stepExecution = new StepExecution("stepName", jobExecution);
        stepExecutionDao.saveStepExecution(stepExecution);
    }

    @Test
    public void testSaveAndFindJobContext() {
        ExecutionContext ctx = new ExecutionContext(Collections.<String, Object> singletonMap("key", "value"));
        jobExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(jobExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(jobExecution);
        assertEquals(ctx, retrieved);
    }

    @Test
    public void testSaveAndFindEmptyJobContext() {

        ExecutionContext ctx = new ExecutionContext();
        jobExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(jobExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(jobExecution);
        assertEquals(ctx, retrieved);
    }

    @Test
    public void testUpdateContext() {

        ExecutionContext ctx = new ExecutionContext(Collections
                .<String, Object> singletonMap("key", "value"));
        jobExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(jobExecution);

        ctx.putLong("longKey", 7);
        contextDao.updateExecutionContext(jobExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(jobExecution);
        
//        Set<Entry<String, Object>> entrySet1 = ctx.entrySet();
//        Set<Entry<String, Object>> entrySet2 = retrieved.entrySet();        
//        assertEquals(ctx, retrieved);
        assertEquals(7, retrieved.getLong("longKey"));
    }

    @Test
    public void testSaveAndFindStepContext() {

        ExecutionContext ctx = new ExecutionContext(Collections.<String, Object> singletonMap("key", "value"));
        stepExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(stepExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(stepExecution);
        assertEquals(ctx, retrieved);
    }

    @Test
    public void testSaveAndFindEmptyStepContext() {

        ExecutionContext ctx = new ExecutionContext();
        stepExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(stepExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(stepExecution);
        assertEquals(ctx, retrieved);
    }

    @Test
    public void testUpdateStepContext() {

        ExecutionContext ctx = new ExecutionContext(Collections.<String, Object> singletonMap("key", "value"));
        stepExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(stepExecution);

        ctx.putLong("longKey", 7);
        contextDao.updateExecutionContext(stepExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(stepExecution);
//        assertEquals(ctx, retrieved);
        assertEquals(7, retrieved.getLong("longKey"));
    }

    @Test
    public void testStoreInteger() {
        ExecutionContext ec = new ExecutionContext();
        ec.put("intValue", new Integer(343232));
        stepExecution.setExecutionContext(ec);
        contextDao.saveExecutionContext(stepExecution);
        ExecutionContext restoredEc = contextDao.getExecutionContext(stepExecution);
        assertEquals(ec, restoredEc);
    }
    
}
