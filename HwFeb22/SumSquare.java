
public class SumSquare implements Runnable
{
    protected int startNum;
    protected int range;
    volatile static int sumSquare;

    SumSquare(int startNum, int range)
    {
        this.startNum = startNum;
        this.range = range;
    }

    public static void main(String[] args) throws InterruptedException
    {
        int threadNum = 10;
        int sum;
        int sumSquare;
        int squareSum;

        Thread mThread[] = new Thread[threadNum];

        for(int i = 0; i < threadNum; i++)
        {            
            mThread[i] = new Thread( new SumSquare((i + 1) * threadNum, ((i + 1) * threadNum) + 10));
        }
        for(int i = 0; i < threadNum; i++)
        {
            mThread[i].start();
        }
        for(int i = 0; i < threadNum; i++)
        {
            mThread[i].join();
        }

        sumSquare = SumSquare.sumSquare;

        sum = sum(0, threadNum * threadNum);

        squareSum = sum * sum;

        System.out.println("SumSquare: " + sumSquare + "\nSquareSum: " + squareSum);
        System.out.println(squareSum + " - " + sumSquare + " = " + (squareSum - sumSquare));

    }
    
    @Override
    public void run() 
    {
        SumSquare.sumSquare += sumSquare(startNum, range);
    }

    public int sumSquare(int startNum, int range)
    {
        int sumSquare = 0;
        
        for(int i = startNum; i <= range; i++)
        {
            sumSquare += i * i;
        }

        return sumSquare;
    }

    public static int sum(int startNum, int range)
    {
        int sum = 0;
        
        for(int i = startNum; i <= range; i++)
        {
            sum += i;
        }

        return sum;
    }
    
}