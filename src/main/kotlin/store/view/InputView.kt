package store.view

import camp.nextstep.edu.missionutils.Console
import store.model.Inventory
import store.model.Product
import store.model.Promotion
import store.utils.ErrorHandler.inputErrorHandler
import java.io.File
import java.io.IOException
import camp.nextstep.edu.missionutils.DateTimes.now
import store.utils.ErrorHandler.getErrorMessage
import java.time.LocalDate

object InputView {

    private const val PRODUCTS_FILE = "./src/main/resources/products.md"
    private const val PROMOTIONS_FILE = "./src/main/resources/promotions.md"
    private const val FILE_READ_ERROR = "파일을 읽는 중 오류가 발생했습니다."
    private const val INVALID_PURCHASE_REQUEST = "올바르지 않은 구매 형식입니다."
    private const val PURCHASE_SCRIPT = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"
    private const val INVALID_QUANTITY = "수량은 숫자로만 입력 가능합니다."

    private fun getFiieLines(path: String): List<String>? {
        try {
            val file = File(path)
            val lines = file.readLines()
            return lines
        } catch (e: IOException) {
            inputErrorHandler(FILE_READ_ERROR, { })
            return null
        }
    }

    fun getPromotions() : List<Promotion> {
        val promotions = mutableListOf<Promotion>()
        val lines = getFiieLines(PROMOTIONS_FILE)

        lines?.drop(1)?.forEach { line ->
            val parts = line.split(",")
            promotions.add(Promotion(parts[0], parts[1].toInt(), parts[2].toInt(), LocalDate.parse(parts[3]), LocalDate.parse(parts[4])))
        }

        return promotions
    }

    fun getProducts(promotions : List<Promotion>) : List<Product> {
        val products = mutableListOf<Product>()
        val lines = getFiieLines(PRODUCTS_FILE)

        lines?.drop(1)?.forEach { line ->
            val parts = line.split(",")
            if(parts.size == 4) products.add(Product(parts[0], parts[1].toInt(), parts[2].toInt(), findPromotionByName(promotions, parts[3])))
            else products.add(Product(parts[0], parts[1].toInt(), parts[2].toInt()))
        }

        return products
    }

    private fun findPromotionByName(promotions: List<Promotion>, name: String): Promotion? {
        return promotions.find { it.name == name }
    }

    fun getPurchase(inventory: Inventory) {
        println(PURCHASE_SCRIPT)
        val input = Console.readLine()
        val purchases = input.split(",")
        try {
            validatePurchase(inventory, purchases)
        }
        catch (e : Exception) {
            println(e.message)
            getPurchase(inventory)
        }

    }

    private fun validatePurchase(inventory: Inventory, purchases : List<String>) {
            purchases.forEach { purchase ->
                val regex = "\\[([a-zA-Z가-힣]+)-(\\d+)\\]".toRegex()
                val matches = regex.findAll(purchase)
                require(matches.any()) { getErrorMessage(INVALID_PURCHASE_REQUEST) }

                matches.forEach { matchResult ->
                    val productName = matchResult.groupValues[1]
                    inventory.containsProductName(productName)
                    val quantity = matchResult.groupValues[2].toIntOrNull() ?: throw IllegalArgumentException(getErrorMessage(INVALID_QUANTITY))
                    inventory.checkQuantity(productName, quantity)
                }
            }
    }



}