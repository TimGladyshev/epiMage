package com.example.finalbackend.worker;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.security.AccessControlException;

import org.springframework.stereotype.Service;
import sun.security.util.SecurityConstants;

@Service
public class ocpuExecutor {
    private ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public void execute(ProcessData data) {
        executorService.execute(data);
    }
}
