package akkaHW2017F;

import java.io.Serializable;

public class Messages {
    public static class fileMessage implements Serializable{

        public fileMessage(String firstPart, String secondPart) {
            this.firstPart = firstPart;
            this.secondPart = secondPart;
        }

        private final String firstPart;
        private final String secondPart;

        public String getFirstPart() {
            return firstPart;
        }

        public String getSecondPart() {
            return secondPart;
        }
    }

    public static class feedBack implements Serializable{
        private final int num;

        public feedBack(int intNum) {
            num = intNum;
        }

        public int getFeedBack() {
            return num;
        }
    }

    public static class partMessage implements Serializable{
        private final String s;

        public partMessage(String s) {
            this.s = s;
        }

        public String getMessage() {
            return s;
        }
    }

    public static class resultMessage implements Serializable {
        private final double estimate;
        private final int real;

        public resultMessage(double estimate, int real) {
            this.estimate = estimate;
            this.real = real;
        }

        public double getEstimate() {
            return estimate;
        }

        public int getReal() {
            return real;
        }
    }
}