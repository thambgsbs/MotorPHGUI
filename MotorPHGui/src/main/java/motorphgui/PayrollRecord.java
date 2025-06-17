package motorphgui;

public class PayrollRecord {
    private String employeeId;
    private double basicSalary;
    private double sss;
    private double philHealth;
    private double pagibig;
    private double tax;
    private double totalDeductions;
    private double netPay;

    public PayrollRecord(String employeeId, double basicSalary, double sss, double philHealth, double pagibig, double tax, double totalDeductions, double netPay) {
        this.employeeId = employeeId;
        this.basicSalary = basicSalary;
        this.sss = sss;
        this.philHealth = philHealth;
        this.pagibig = pagibig;
        this.tax = tax;
        this.totalDeductions = totalDeductions;
        this.netPay = netPay;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getSss() {
        return sss;
    }

    public double getPhilHealth() {
        return philHealth;
    }

    public double getPagibig() {
        return pagibig;
    }

    public double getTax() {
        return tax;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public double getNetPay() {
        return netPay;
    }
}
