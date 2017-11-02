package com.artronics.cashplay.api

import java.io.Serializable
import java.util.*
import javax.persistence.*


@MappedSuperclass
open class BaseModel : Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @GeneratedValue(strategy=GenerationType.AUTO)
//    @SequenceGenerator(name="seq-gen",sequenceName="MY_SEQ_GEN", initialValue=205, allocationSize=12)
//    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq-gen")
    @Column(name = "id", updatable = false, nullable = false)
    var id: Long? = null

    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    var createdAt: Date? = null

    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    var updatedAt: Date? = null

    @PrePersist
    fun onPersist() {
        updatedAt = Date()
        createdAt = updatedAt
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Date()
    }
}
