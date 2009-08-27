package wizest.fx.util;

import java.util.LinkedList;
import java.util.Random;

/**
 * �ϳ��� �Է°� �ϳ��� ����� �������� ���������� �������� ��⿭�� ���� queue�̴�.
 * ParallerQuenuEntry�� Line Id�� ���� �������� ��⿭�� ����� �� ���� '����� ����'�� '���� ����'�� �������� �ȴ�.
 * ��, ���� line id�� ���� ��� ������ ó���� ������ �ٸ� line id�� ������ ���� ���� ���� ���� ������ ó���ȴ�.
 * �� ���� ����� line id�� ���Ӱ� �Էµ� ��� ���ο� ���� �����Ǹ� �ϳ��� ���� entry�� ��� �Ǹ� ��ü �ٿ��� ���ŵȴ�.
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
     * entry�� line id�� ���� �Ͽ� ������ �� �ڿ� ����ִ´�.
     * ���� line id�� ���� ��� ���ο� line�� �����Ѵ�.
     * @param entry
     */
    public synchronized void put(ParallelQueueEntry entry)
    {
        Object lineId=entry.getParallelQueueLineId();
        int idx=lines.indexOf(lineId);

        WaitingLine line=null;
        // ���� ��
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
     * ���ܷ� Ư���� ���� ó���ؾ� �� ��� ���� �������� �д� ��.��
     * @param entry
     */
    public synchronized void putFirst(ParallelQueueEntry entry)
    {
        Object lineId=entry.getParallelQueueLineId();
        int idx=lines.indexOf(lineId);

        WaitingLine line=null;
        // ���� ��
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
     * random �ϰ� line�� �����ؼ� �� ������ ������ �ش�.
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
     * comparator�� ���� equals() �� �����ϴ� �Ѱ��� entry�� �����ȴ�.
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
     * ���ǿ� �ش��ϴ� ��� entry�� �����.
     * @param tc
     * @return ���� entry ��
     */
    public synchronized int removeAll(TargetCondition tc)
    {
        int cnt =0;
        for(int i=0;i < lines.size();++i) {
            WaitingLine line=(WaitingLine)lines.get(i);
            for(int j=0;j < line.size();++j) {
                if(tc.isTarget(line.get(j))) {
                	@SuppressWarnings("unused")
                    ParallelQueueEntry entry=(ParallelQueueEntry)line.remove(j--);  // ���ﶧ �������� ������ �ǹǷ� index�� �ϳ� ����
                    if(line.isEmpty())  {
                        lines.remove(i--); // ���ﶧ �������� ������ �ǹǷ� index�� �ϳ� ����
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
        // ���� ��
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
     * line id�� ������ (����:entry�� �Ű澲�� �ʴ´�.)
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
