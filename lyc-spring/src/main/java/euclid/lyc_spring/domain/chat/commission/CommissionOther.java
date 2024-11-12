package euclid.lyc_spring.domain.chat.commission;

import euclid.lyc_spring.dto.request.InfoRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
public class CommissionOther {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column
    private LocalDate dateToUse;

    @Column
    private LocalDate desiredDate;

    @Column(nullable = false)
    private Integer minPrice;

    @Column(nullable = false)
    private Integer maxPrice;

    @Column(columnDefinition = "text")
    private String text;

    @Column
    private Boolean isShareClothesList;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", nullable = false)
    private Commission commission;

    protected CommissionOther() {}

    @Builder
    public CommissionOther(LocalDate dateToUse, LocalDate desiredDate, Integer minPrice, Integer maxPrice, String text, Boolean isShareClothesList) {
        this.dateToUse = dateToUse;
        this.desiredDate = desiredDate;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.text = text;
        this.isShareClothesList = isShareClothesList;
    }

    // Method

    public void reloadDateToUse(LocalDate dateToUse){
        this.dateToUse = dateToUse;
    }

    public void reloadDesiredDate(LocalDate desiredDate){
        this.desiredDate = desiredDate;
    }

    public void reloadMinPrice(Integer minPrice){
        this.minPrice = minPrice;
    }

    public void reloadMaxPrice(Integer maxPrice){
        this.maxPrice = maxPrice;
    }

    public void reloadText(String text){
        this.text = text;
    }

    public void reloadIsShareClothesList(Boolean isShareClothesList){this.isShareClothesList = isShareClothesList;}
}
