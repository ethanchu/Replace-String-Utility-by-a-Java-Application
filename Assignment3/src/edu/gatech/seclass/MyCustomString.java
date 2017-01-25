package edu.gatech.seclass;

/**
 * Created by swengineer on 1/23/17.
 */
public class MyCustomString implements MyCustomStringInterface {

    private String string;

    public String getString()
    {
        return string;
    }

    public void setString(String string)
    {
        this.string = string;
    }

    public int countNumbers()
    {
        if (this.string.length() != 0) {

            int count = 0;
            boolean previousbit = false;

            for (int i = 0; i < this.string.length(); i++) {
                if (Character.isDigit(this.string.charAt(i))) {
                    if (!previousbit) {
                        count++;
                        previousbit = true;
                    }
                } else {
                    previousbit = false;
                }
            }

            return count;
        } else {
                return 0;
            }
    }


    public String removeEveryNthCharacter(int n, boolean maintainSpacing)
    {
        if (n > this.string.length()) {
            throw new MyIndexOutOfBoundsException();
        }
        else if (n <= 0){
            throw new IllegalArgumentException();
        }
        else {
            String tmp = "";
            int loop =  (int)(this.string.length()/n);
            for (int i = 0; i < loop+1; i++) {
                if (!maintainSpacing)
                    tmp = tmp + this.string.substring(i * n, Math.min(string.length(), i * n + n - 1));
                else
                    if (i == loop) {
                        tmp = tmp + this.string.substring(i * n, Math.min(string.length(), i * n + n - 1));
                    } else {
                        tmp = tmp + this.string.substring(i * n, Math.min(string.length(), i * n + n - 1)) + " ";
                    }
            }
            return tmp;
        }
    }

    public void convertDigitsToNamesInSubstring(int startPosition, int endPosition) {
        if (this.string == null)
            throw new NullPointerException();
        if (startPosition > endPosition || endPosition > this.string.length()){
            throw new MyIndexOutOfBoundsException();
        }
        if (startPosition<=endPosition && startPosition < 1) {
            throw new IllegalArgumentException();
        }
        String start = this.string.substring(0,startPosition-1);
        String end = this.string.substring(endPosition,string.length());
        String modify = this.string.substring(startPosition-1,endPosition);
        String numberChar[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String numbers[] = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

        boolean previousbit = false;

        for (int i = 0; i < modify.length(); i++) {
            if (Character.isDigit(modify.charAt(i))) {
                if (!previousbit) {

                    previousbit = true;
                }
                else {
                    modify = modify.substring(0,i) + "-" + modify.substring(i,modify.length());
                    previousbit  = false;
                }
            } else {
                previousbit = false;
            }
        }

        for(int i = 0; i < numbers.length; i++) {
            modify = modify.replaceAll(numberChar[i], numbers[i]);
        }

        this.string = start + modify + end;
    }

}
