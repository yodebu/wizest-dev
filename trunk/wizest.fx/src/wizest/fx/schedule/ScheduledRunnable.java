package wizest.fx.schedule;

import java.io.Serializable;

/**
 * job을 serialization 하기 위해서 serializable 만 추가로 붙인 runnable
 */

public interface ScheduledRunnable extends Runnable,Serializable
{
}