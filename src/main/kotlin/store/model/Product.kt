package store.model

class Product(
     val name: String,
     val price: Int,
     val quntatiy: Int,
     val promotion: Promotion? = null
) {

}