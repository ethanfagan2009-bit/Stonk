//They would loose money because the market crashed.
// yes, both where during a recession
//    System.out.println("lowest: "+monthName(months[indexOfLowest(prices)])+" "+years[indexOfLowest(prices)]+" "+prices[indexOfLowest(prices)]);
     //  System.out.println("highest: "+monthName(months[indexOfHighest(prices)])+" "+years[indexOfHighest(prices)]+" "+prices[indexOfHighest(prices)]);
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {
	public static final double START_CASH = 10000.0;
	public static final double FEE = 10.0;
    public static void main(String[] args) throws FileNotFoundException {
		//Scanner in = new Scanner(new File("zigzag.txt"));
        Scanner in = new Scanner(new File("aapl_2000_2010.txt"));
        String symbol = in.next();
        int n = in.nextInt();
		System.out.println(symbol+n);
		int[] years=new int[n];
		int[] months=new int[n];
		double[] prices=new double[n];
		
    
        loadData(in, years, months, prices);
        double result= simulate(years,months,prices);
        System.out.println("Final value: " + result);
        
    
    }
    
    
    
    
    
    public static double simulate(int[] years, int[] months, double[] prices) {
    double cash = START_CASH;
    int shares = 0;
    double boughtAt = 0.0;

    for (int i = 0; i < prices.length; i++) {
        double[] history = Arrays.copyOf(prices, i + 1);
        String action = decide(history, shares > 0, boughtAt);

        if (action.equals("BUY") && shares == 0) {
            int canBuy = (int) ((cash - FEE) / prices[i]);
            if (canBuy > 0) {
                shares = canBuy;
                cash = cash - shares * prices[i] - FEE;
                boughtAt = prices[i];
                System.out.printf("%s %d: BUY %d shares at $%.2f%n",
                        monthName(months[i]), years[i], shares, prices[i]);
            }
        } else if (action.equals("SELL") && shares > 0) {
            cash = cash + shares * prices[i] - FEE;
            System.out.printf("%s %d: SELL %d shares at $%.2f%n",
                    monthName(months[i]), years[i], shares, prices[i]);
            shares = 0;
            boughtAt = 0.0;
        }
    }
    return cash + shares * prices[prices.length - 1];
	}
	public static double avrage(double[] history) {
		
		//double avr=0;
		//int range=0;
		//if(history.length<6) range= (int)history.length; else range=6;
		//for(int i=0; i<range;i++) {
		//	avr+=history[history.length+i-range];
		//}
		//return avr/range;
		
		double avr=0;
		int range=0;
		if(history.length<6) range= (int)history.length; else range=6;
		for(int i=0; i<range;i++) {
			avr+=history[history.length+i-range];
		}
		return avr/range;
	}
	
	public static double avrageDiff(double[] history) {
		double avr=0;
		int range=0;
		if(history.length<6) range= (int)history.length; else range=6;
		for(int i=0; i<range;i++) {
			avr+=avrage(history)-history[i];
		}
		return avr/range;
	}
	
	public static String decide(double[] history, boolean holding, double boughtAt) {
		float aproxNext=0;
		if(history.length>24) {
			aproxNext=trend(history, history.length/12);
		}
		//double avr= avrage(history);
		//double coin = history[history.length-1];
		//System.out.println(avr +" "+ coin);
		////if(coin<avr) return "BUY";
		//if(aproxNext>coin) return "hold";
		//if(coin/avr>1.1 && coin>boughtAt) return "SELL";
		////if(holding) return "hold"
		////if(coin/avr<=1.2 && coin/avr>=0.8) return "BUY";
		//if(coin<avr && coin/avr<=0.9) return "BUY";
		
		
		double avrdiff= avrageDiff(history);
		double avr= avrage(history);
		double coin = history[history.length-1];
		double diff=avr-coin;
		System.out.println(avr +" "+ coin);
		//if(coin<avr) return "BUY";
		if(aproxNext>coin) return "hold";
		if(avrdiff<diff && coin>boughtAt) return "SELL";
		//if(holding) return "hold"
		//if(coin/avr<=1.2 && coin/avr>=0.8) return "BUY";
		if(avrdiff>diff) return "BUY";
		
		
		return "HOLD";
	}

    
    public static float trend(double[] history, int month) {
		int count=0;
		int result=0;
		
		for(int i=0; i<history.length;i++) {
			if(i%12==month) {
				if(count>0) {
					if(history[i]/(result/count)>2 || history[i]/(result/count)<0.4) {
						count++;
						result+=history[i];
					}
				} else {
					count++;
					result+=history[i];
				}
			}
		}
		if(result>0 && count>0) return result/count;
		return 1;
	}
    
    
    
    
    
    
    public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
		for(int i=0;i<years.length;i++) {
			years[i]=in.nextInt();
			months[i]=in.nextInt();
			prices[i]=in.nextDouble();
		}
	}
	public static String monthName(int month) {
		month=month%12;
		String[] names = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		return names[month];
		
	}
	
	public static void printData(int[] years, int[] months, double[] prices) {
		for(int i=0;i<years.length;i++) {
			System.out.println(monthName(months[i])+" "+years[i]+" "+prices[i]);
			
		}
	}
	
	public static int indexOfLowest(double[] prices) {
		int result=0;
		double lowest=prices[0];
		for(int i=0; i<prices.length; i++) {
			if(lowest>prices[i]) {
				 result=i;
				 lowest=prices[i];
			 }
		}
		return result;
	}
	public static int indexOfHighest(double[] prices) {
		int result=0;
		double highest=prices[0];
		for(int i=0; i<prices.length; i++) {
			if(highest<prices[i]) {
				 result=i;
				 highest=prices[i];
			 }
		}
		return result;
	}
}
