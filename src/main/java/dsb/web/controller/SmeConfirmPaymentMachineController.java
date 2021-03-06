package dsb.web.controller;

import dsb.web.controller.beans.ConfirmBean;
import dsb.web.domain.TokenPaymentMachine;
import dsb.web.repository.TokenPaymentMachineRepository;
import dsb.web.service.RequestPaymentMachineService;
import dsb.web.service.SmeDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@SessionAttributes(AttributeMapping.LOGGED_IN_EMPLOYEE)
public class SmeConfirmPaymentMachineController {
   private SmeDashboardService smeDashboardService;
   private RequestPaymentMachineService requestPaymentMachineService;
   private Logger logger = LoggerFactory.getLogger(SmeConfirmPaymentMachineController.class);

    @Autowired
    public SmeConfirmPaymentMachineController(SmeDashboardService smeDashboardService, RequestPaymentMachineService requestPaymentMachineService) {
        this.smeDashboardService = smeDashboardService;
        this.requestPaymentMachineService = requestPaymentMachineService;
    }

    public SmeConfirmPaymentMachineController() {
    }

    @GetMapping("paymentmachine-request-overview")
    public String paymentMachineRequestOverview(Model model) {
        List<TokenPaymentMachine> getPaymentMachineRequests = smeDashboardService.getAllPaymentMachineRequests();
        model.addAttribute("paymentMachineRequests", getPaymentMachineRequests);
        return "sme-payment-machine-request-overview";
    }

    @PostMapping("sme-confirm-paymentmachine-request")
    public String paymentMachineRequestOverviewHandler(
            @ModelAttribute("tokenID") int tokenID, Model model) {
        TokenPaymentMachine tokenPaymentMachine = requestPaymentMachineService.generateSecCodeToken(tokenID);
        if (tokenPaymentMachine.getSecurityCode() != 0) {
            model.addAttribute(new ConfirmBean("Pinautomaataanvraag koppelcode", "De pinautomaat dient met" +
                    " de volgende code geactiveerd te worden: " + tokenPaymentMachine.getSecurityCode() + ".", "paymentmachine-request-overview", "OK"));
            return "confirm";
        }
        return paymentMachineRequestOverview(model);
    }
}
