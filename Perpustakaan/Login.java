public class Login {
    private String username;
    private String password;
    private String role;

    public Login(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    public String  getUsername()            { return username; }
    public String  getRole()                { return role; }
    public boolean cekPassword(String input){ return password.equals(input); }
}
