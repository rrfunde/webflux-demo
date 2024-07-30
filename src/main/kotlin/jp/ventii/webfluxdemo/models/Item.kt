package jp.ventii.webfluxdemo.models



import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("items")
data class Item(
    @Id val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int
) {
    fun isValid(): Boolean {
        return name.isNotBlank() && description.isNotBlank()
    }
}