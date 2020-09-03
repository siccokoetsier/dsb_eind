package dsb.web.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Company {

    @Id
    @GeneratedValue
    private int companyId;
    private String name;
    private String KVKno;
    private String BTWno;

    public Company(int companyId, String name, String KVKno, String BTWno) {
        this.companyId = companyId;
        this.name = name;
        this.KVKno = KVKno;
        this.BTWno = BTWno;
    }

    public Company(String name, String KVKno, String BTWno) {
        this.name = name;
        this.KVKno = KVKno;
        this.BTWno = BTWno;
    }

    public Company() {
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", name='" + name + '\'' +
                ", KVKno='" + KVKno + '\'' +
                ", BTWno='" + BTWno + '\'' +
                '}';
    }

    public int getcompanyId() {
        return companyId;
    }

    public void setcompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKVKno() {
        return KVKno;
    }

    public void setKVKno(String KVKno) {
        this.KVKno = KVKno;
    }

    public String getBTWno() {
        return BTWno;
    }

    public void setBTWno(String BTWno) {
        this.BTWno = BTWno;
    }
}