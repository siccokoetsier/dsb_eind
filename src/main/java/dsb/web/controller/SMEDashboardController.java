package dsb.web.controller;

import dsb.web.domain.*;

import dsb.web.repository.SMEAccountRepository;
import dsb.web.repository.SectorRepository;
import dsb.web.service.RequestPaymentMachineService;
import dsb.web.service.SmeDashboardService;
import dsb.web.service.service_helpers.SMETransactionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;
import java.util.Map;


@Controller
@SessionAttributes(AttributeMapping.LOGGED_IN_EMPLOYEE)
public class SMEDashboardController {
    private SmeDashboardService smeDashboardService;
    private SMEAccountRepository smeAccountRepository;
    private SectorRepository sectorRepository;
    private RequestPaymentMachineService requestPaymentMachineService;
    private Logger logger = LoggerFactory.getLogger(SMEDashboardController.class);

    @Autowired
    public SMEDashboardController(SmeDashboardService smeDashboardService, SMEAccountRepository smeAccountRepository,SectorRepository sectorRepository) {
        this.smeAccountRepository = smeAccountRepository;
        this.smeDashboardService = smeDashboardService;
        this.sectorRepository = sectorRepository;
    }


    public SMEDashboardController() {
    }

    @GetMapping("sme_employee_dashboard")
    public String smeDashboardOverview(@ModelAttribute(AttributeMapping.LOGGED_IN_EMPLOYEE) Employee employee, Model model) {

        List<SMETransactionHelper> top10MostValuedSMECustomers = smeDashboardService.findTop10SMETransactions();
        Map<Sector, Integer> averageTop10BySector = smeDashboardService.averageBySector();
        List<TokenPaymentMachine> getAllLinkRequests = smeDashboardService.getAllPaymentMachineRequests();
        List<SMEAccount> top10Balance = smeDashboardService.getTop10bySmeBalance();

        model.addAttribute("selectedEmployee", employee.printWholeName());
        model.addAttribute("linkRequestList", getAllLinkRequests);
        model.addAttribute("transactionsList", top10MostValuedSMECustomers);
        model.addAttribute("balances", top10Balance);
        model.addAttribute("averageBalanceBySector", averageTop10BySector);
        return "sme_employee_dashboard";
    }
}


