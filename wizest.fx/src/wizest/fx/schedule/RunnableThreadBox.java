package wizest.fx.schedule;

import java.util.logging.Level;
import java.util.logging.Logger;

import wizest.fx.logging.LogBroker;


/**
 * scheduler 가 runnable을 실행할 때 log를 남기기위한 껍데기 Thread로 사용한다.
 */

public class RunnableThreadBox extends Thread
{
    Logger logger = null;
    Runnable runner=null;

    public RunnableThreadBox(Runnable r)
    {
        super(r);
        this.logger = LogBroker.getLogger(r);
        this.runner=r;
    }
    public void run()
    {
        logger.log(Level.INFO,"Thread started: "+runner);
        try
        {
            super.run();
        }
        catch(Throwable t)
        {
            logger.log(Level.SEVERE,"unexpected error",t);
        }
        logger.log(Level.INFO,"Thread finished: "+runner);
    }
}