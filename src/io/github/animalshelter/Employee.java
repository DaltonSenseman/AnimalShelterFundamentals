public class Employee {

    private int employeeNum;
    private String firstName;
    private String lastName;
    private String jobClass;

    public Employee(int employeeNum, String firstName, String lastName, String jobClass) {
        this.employeeNum = employeeNum;
        this.firstName= firstName;
        this.lastName = lastName;
        this.jobClass = jobClass;
    }

    public int getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(int employeeNum) {
        this.employeeNum = employeeNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }
}
