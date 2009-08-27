package wizest.fx.util;

import java.util.LinkedList;
import java.util.Random;

/**
 * 하나의 입력과 하나의 출력을 가지지만 내부적으로 여러개의 대기열을 가진 queue이다.
 * ParallerQuenuEntry의 Line Id에 따라서 여러줄의 대기열을 만들고 각 줄은 '평등한 관계'로 '같은 비율'로 꺼내지게 된다.
 * 즉, 같은 line id를 가질 경우 선착순 처리를 하지만 다른 line id들 끼리는 선후 관계 없이 같은 비율로 처리된다.
 * 각 줄의 운용은 line id가 새롭게 입력될 경우 새로운 줄이 생성되며 하나의 줄의 entry가 비게 되면 전체 줄에서 제거된다.
 *
 */

public class ParallelQueue
{
    private LinkedList lines;
    private Random rand;

    public ParallelQueue()
    {
        this.rand=new Random(System.currentTimeMillis());
        this.lines=new LinkedList();
    }

    public synchronized boolean isEmpty()
    {
        return (lines.size()==0);
    }

    /**
     * entry의 line id를 참고 하여 적당한 줄 뒤에 집어넣는다.
     * 기존 line id가 없을 경우 새로운 line을 생성한다.
     * @param entry
     */
    public synchronized void put(ParallelQueueEntry entry)
    {
        Object lineId=entry.getParallelQueueLineId();
        int idx=lines.indexOf(lineId);

        WaitingLine line=null;
        // 없을 때
        if(idx < 0) {
            line=new WaitingLine(entry.getParallelQueueLineId());
            lines.add(line);
        }
        else {
            line=(WaitingLine)lines.get(idx);
        }

        line.addLast(entry);
    }

    /**
     * 예외로 특별히 먼저 처리해야 할 경우 제일 앞쪽으로 둔다 ㅡ.ㅡ
     * @param entry
     */
    public synchronized void putFirst(ParallelQueueEntry entry)
    {
        Object lineId=entry.getParallelQueueLineId();
        int idx=lines.indexOf(lineId);

        WaitingLine line=null;
        // 없을 때
        if(idx < 0) {
            line=new WaitingLine(entry.getParallelQueueLineId());
            lines.add(line);
        }
        else {
            line=(WaitingLine)lines.get(idx);
        }

        line.addFirst(entry);
    }

    /**
     * random 하게 line을 선택해서 고른 순서로 꺼내어 준다.
     * @return null if empty
     */
    public synchronized ParallelQueueEntry get()
    {
        int size=lines.size();
        if(size == 0)
            return null;

        int idx=rand.nextInt(size);
        WaitingLine line=(WaitingLine)lines.get(idx);
        ParallelQueueEntry entry=(ParallelQueueEntry)line.removeFirst();

        if(line.isEmpty())
            lines.remove(idx);
        return entry;
    }

    /**
     * comparator에 의해 equals() 을 만족하는 한개의 entry가 삭제된다.
     * @param removeCondition
     * @return null if not found a entry to match the target condition.
     */
    public synchronized ParallelQueueEntry remove(TargetCondition tc)
    {
        for(int i=0;i < lines.size();++i) {
            WaitingLine line=(WaitingLine)lines.get(i);
            for(int j=0;j < line.size();++j) {
                if(tc.isTarget(line.get(j))) {
                    ParallelQueueEntry entry=(ParallelQueueEntry)line.remove(j);
                    if(line.isEmpty())
                        lines.remove(i);
                    return entry;
                }
            }
        }

        return null;

    }

    /**
     * 조건에 해당하는 모든 entry를 지운다.
     * @param tc
     * @return 지운 entry 수
     */
    public synchronized int removeAll(TargetCondition tc)
    {
        int cnt =0;
        for(int i=0;i < lines.size();++i) {
            WaitingLine line=(WaitingLine)lines.get(i);
            for(int j=0;j < line.size();++j) {
                if(tc.isTarget(line.get(j))) {
                	@SuppressWarnings("unused")
                    ParallelQueueEntry entry=(ParallelQueueEntry)line.remove(j--);  // 지울때 왼쪽으로 쉬프팅 되므로 index를 하나 줄임
                    if(line.isEmpty())  {
                        lines.remove(i--); // 지울때 왼쪽으로 쉬프팅 되므로 index를 하나 줄임
                    }
                    ++cnt;
                }
            }
        }

        return cnt;
    }


    public synchronized boolean remove(ParallelQueueEntry entry)
    {
        Object lineId=entry.getParallelQueueLineId();
        int idx=lines.indexOf(lineId);
        // 없을 때
        if(idx < 0)
            return false;
        else
            return((WaitingLine)lines.get(idx)).remove(entry);
    }

    public synchronized int size()
    {
        int totalSize=0;
        for(int i=0,length=lines.size();i < length;++i) {
            totalSize+=((WaitingLine)lines.get(i)).size();
        }
        return totalSize;
    }

}

class WaitingLine extends LinkedList
{
    final Object lineId;

    WaitingLine(Object lineId)
    {
        super();
        this.lineId=lineId;
    }

    /**
     * line id가 같은지 (주의:entry는 신경쓰지 않는다.)
     * @param lineId
     * @return
     */
    public boolean equals(Object lineId)
    {
        return lineId.equals(lineId);
    }

    public int hashCode()
    {
        return lineId.hashCode();
    }
}
