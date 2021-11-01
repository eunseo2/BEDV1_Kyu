package org.prgrms.kyu.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "Foods")
@Getter
@NoArgsConstructor
public class Food extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer price;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "store_id")
    private Store store;


    @Builder
    public Food(String name, String description, Integer price, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.image = image;
    }

    public void update(Store store) {
        this.store = store;
    }
}
