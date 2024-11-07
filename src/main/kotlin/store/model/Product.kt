package store.model

class Product(
     val name: String,
     val price: Int,
     val quantity: Int,
     val promotion: Promotion? = null
) {

}