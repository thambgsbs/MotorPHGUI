package motorphgui;

public class DeductionsCalculator {

    public static double computeSSS(double salary) {
        return salary * 0.045; // SSS = 4.5%
    }

    public static double computePhilHealth(double salary) {
        return salary * 0.035; // PhilHealth = 3.5%
    }

    public static double computePagibig(double salary) {
        return salary * 0.02; // Pag-IBIG = 2%
    }

    public static double computeWithholdingTax(double salary) {
        if (salary <= 20833) return 0;
        else if (salary <= 33332) return (salary - 20833) * 0.2;
        else if (salary <= 66666) return 2500 + (salary - 33332) * 0.25;
        else return 10833 + (salary - 66666) * 0.3;
    }

    public static double computeTotalDeductions(double salary) {
        return computeSSS(salary) + computePhilHealth(salary) +
               computePagibig(salary) + computeWithholdingTax(salary);
    }

    public static double computeNetPay(double salary) {
        return salary - computeTotalDeductions(salary);
    }
}
