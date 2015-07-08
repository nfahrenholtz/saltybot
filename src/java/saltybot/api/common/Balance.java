package saltybot.api.common;

public class Balance {
    
    private final Integer balance;
    
    public Balance(final Integer balance) {
        this.balance = balance;
    }
    
    public Integer getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "balance=" + balance +
                '}';
    }
}
