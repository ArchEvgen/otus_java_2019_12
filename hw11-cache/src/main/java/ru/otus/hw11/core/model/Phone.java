package ru.otus.hw11.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "phones")
@ToString(exclude = {"user"})
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phones_seq")
    @SequenceGenerator(name = "phones_seq", sequenceName = "phones_id_sequence")
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;

    @Column(name = "number")
    private String number;
}
