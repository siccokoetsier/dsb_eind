package dsb.web.service;

import dsb.web.domain.*;
import dsb.web.repository.TokenPaymentMachineRepository;
import dsb.web.repository.SMEAccountRepository;
import dsb.web.repository.SectorRepository;
import dsb.web.repository.TransactionRepository;
import dsb.web.service.service_helpers.SMETransactionHelper;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SmeDashboardService {
    private final SMEAccountRepository smeAccountRepository;
    private final TransactionRepository transactionRepository;
    private final SectorRepository sectorRepository;
    private final TokenPaymentMachineRepository tokenPaymentMachineRepository;

    public SmeDashboardService(SMEAccountRepository smeAccountRepository, TransactionRepository transactionRepository,
                               SectorRepository sectorRepository, TokenPaymentMachineRepository tokenPaymentMachineRepository) {
        this.smeAccountRepository = smeAccountRepository;
        this.transactionRepository = transactionRepository;
        this.sectorRepository = sectorRepository;
        this.tokenPaymentMachineRepository = tokenPaymentMachineRepository;
    }

    public List<SMEAccount> getTop10bySmeBalance() {
        List<SMEAccount> smeAccountsList = smeAccountRepository.findTop10ByOrderByBalanceDesc();
        return smeAccountsList;
    }

    public List<TokenPaymentMachine> getAllPaymentMachineRequests() {
        List<TokenPaymentMachine> listRequest = tokenPaymentMachineRepository.findAll();
        List<TokenPaymentMachine> newRequests = new ArrayList<>();
        for (TokenPaymentMachine token : listRequest) {
            if (token.getSecurityCode() == 0) {
                newRequests.add(token);
            }
        }
        return newRequests;
    }

    public Map<SMEAccount, Integer> getSmeTransaction() {
        Map<SMEAccount, Integer> result = new HashMap<>();
        for (SMEAccount account : smeAccountRepository.findAll()) {
            int credits = transactionRepository.countTransactionsByCreditAccount(account);
            int debits = transactionRepository.countTransactionsByDebitAccount(account);
            int total = credits + debits;
            result.put(account, total);

        }
        return result;
    }

    public List<SMETransactionHelper> findTop10SMETransactions() {
        List<SMETransactionHelper> result2 = new ArrayList<>();
        for (Map.Entry<SMEAccount, Integer> entry : getSmeTransaction().entrySet()) {
            Account a = entry.getKey();
            int number = entry.getValue();
            SMETransactionHelper s = new SMETransactionHelper(a, number);
            result2.add(s);
        }
        Collections.sort(result2);
        List<SMETransactionHelper> limited = result2.stream().limit(9).collect(Collectors.toList());

        return limited;
    }

    public Map<Sector, Integer> averageBySector() {
        Map<Sector, Integer> averageTop10BySector = new HashMap<>();
        for (Sector sec : sectorRepository.findAll()) {
            int average = 0;
            List<SMEAccount> list = smeAccountRepository.findAllByCompany_Sector(sec);
            for (SMEAccount acc : list) {
                average += acc.getBalance();
            }
            if (list.size() > 0)
                average = average / list.size();
            averageTop10BySector.put(sec, average);
        }
        TreeMap<Sector, Integer> sorted = new TreeMap<>(averageTop10BySector);

        return sorted;
    }
}
