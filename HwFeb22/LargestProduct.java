
public class LargestProduct implements Runnable
{
    protected int startNum;
    protected int range;
    protected static String tDigNum = "7316717653133062491922511967442657474235534919493496983520312774506326239578318016984801869478851843858615607891129494954595017379583319528532088055111254069874715852386305071569329096329522744304355766896648950445244523161731856403098711121722383113622298934233803081353362766142828064444866452387493035890729629049156044077239071381051585930796086670172427121883998797908792274921901699720888093776657273330010533678812202354218097512545405947522435258490771167055601360483958644670632441572215539753697817977846174064955149290862569321978468622482839722413756570560574902614079729686524145351004748216637048440319989000889524345065854122758866688116427171479924442928230863465674813919123162824586178664583591245665294765456828489128831426076900422421902267105562632111110937054421750694165896040807198403850962455444362981230987879927244284909188845801561660979191338754992005240636899125607176060588611646710940507754100225698315520005593572972571636269561882670428252483600823257530420752963450";
    volatile static int largestProd;
    volatile static String largestSeq;

    LargestProduct(int startNum, int range)
    {
        this.startNum = startNum;
        this.range = range;
    }

    public static void main(String[] args) throws InterruptedException
    {
        int threadNum = 10;

        Thread mThread[] = new Thread[threadNum];
        for(int i = 0; i < threadNum; i++)
        {            
            mThread[i] = new Thread( new LargestProduct((i) * 100, ((i) * 100) + 100));
        }
        for(int i = 0; i < threadNum; i++)
        {
            mThread[i].start();
        }
        for(int i = 0; i < threadNum; i++)
        {
            mThread[i].join();
        }
        
        System.out.print("The largest prod is produced by the sequence: "  + LargestProduct.largestSeq.charAt(0));

        for(int i = 1; i < 13; i++)
        {
            System.out.print(" * " + LargestProduct.largestSeq.charAt(i));
        }

        System.out.println(" = " + LargestProduct.largestProd);
        
    }

    @Override
    public void run() 
    {
        int largestProd = 1;
        String largestProdSeq = largestProd(startNum, range);

        for(int n = 0; n < 13; n++)
        {
            largestProd *= Character.getNumericValue(largestProdSeq.charAt(n));     
        }

        if(largestProd > LargestProduct.largestProd)
        {
            LargestProduct.largestProd = largestProd;
            LargestProduct.largestSeq = largestProdSeq;
        }
    }

    public static String largestProd(int startNum, int range)
    {
        int largestProd = 0;
        int prodBuf;
        char prodSeqBuf[] = new char[13];
        String largestProdSeq = "";

        for(int i = startNum; i < range; i++)
        {
            prodBuf = 1;
            for(int n = 0; n < 13; n++)
            {
                if(i + n < 1000)
                {
                    prodBuf *= Character.getNumericValue(tDigNum.charAt(i + n));
                    prodSeqBuf[n] = tDigNum.charAt(i + n);
                }
                else
                {
                    prodBuf = -1;
                }
            }
            if(prodBuf > largestProd)
            {
                largestProd = prodBuf;
                largestProdSeq = new String(prodSeqBuf);
            }
        }

        return largestProdSeq;

    }
    
}
