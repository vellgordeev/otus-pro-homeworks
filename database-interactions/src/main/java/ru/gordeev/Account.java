package ru.gordeev;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@RepositoryTable(title = "accounts")
public class Account {

    @RepositoryIdField
    @RepositoryField(name = "id")
    private Long id;

    @RepositoryField(name = "amount")
    private Long amount;

    @RepositoryField(name = "account_type")
    private String accountType;

    @RepositoryField(name = "status")
    private String status;

    public Account(Long amount, String accountType, String status) {
        this.amount = amount;
        this.accountType = accountType;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                ", accountType='" + accountType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
