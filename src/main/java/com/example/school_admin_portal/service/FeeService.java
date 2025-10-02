package com.example.school_admin_portal.service;

import com.example.school_admin_portal.entity.FeeEntity;
import com.example.school_admin_portal.repository.FeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeeService {

    private final FeeRepository feeRepository;

    public FeeService(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    public List<FeeEntity> getFeesByStudentId(Long studentId) {
        return feeRepository.findByStudentId(studentId);
    }

    public FeeEntity getFeeById(Long id) {
        return feeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee record not found"));
    }

    public List<FeeEntity> getPendingFees(Long studentId) {
        return feeRepository.findByStudentIdAndStatus(studentId, "Pending");
    }

    public List<FeeEntity> getPaidFees(Long studentId) {
        return feeRepository.findByStudentIdAndStatus(studentId, "Paid");
    }

    public Double getTotalFeeAmount(Long studentId) {
        Double total = feeRepository.getTotalFeeByStudentId(studentId);
        return total != null ? total : 0.0;
    }

    public Double getPaidAmount(Long studentId) {
        Double paid = feeRepository.getPaidFeeByStudentId(studentId);
        return paid != null ? paid : 0.0;
    }

    public Double getPendingAmount(Long studentId) {
        Double pending = feeRepository.getPendingFeeByStudentId(studentId);
        return pending != null ? pending : 0.0;
    }

    public Map<String, Double> getFeeBreakdown(Long studentId) {
        List<FeeEntity> fees = feeRepository.findByStudentId(studentId);
        return fees.stream()
                .collect(Collectors.groupingBy(
                        FeeEntity::getFeeType,
                        Collectors.summingDouble(FeeEntity::getAmount)
                ));
    }

    public FeeEntity saveFee(FeeEntity fee) {
        return feeRepository.save(fee);
    }

    public FeeEntity payFee(Long feeId) {
        FeeEntity fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee record not found"));
        fee.setStatus("Paid");
        fee.setPaidDate(LocalDate.now());
        fee.setReceiptNumber("RCP" + System.currentTimeMillis());
        return feeRepository.save(fee);
    }

    public void deleteFee(Long id) {
        feeRepository.deleteById(id);
    }
}
