package store.view

import camp.nextstep.edu.missionutils.Console
import store.model.Inventory
import store.model.Product
import store.model.Promotion
import store.model.PromotionStatus
import java.io.File
import store.model.PurchaseItem
import store.utils.ErrorHandler.getErrorMessage
import java.time.LocalDate

object InputView {

    private const val PRODUCTS_FILE_PATH = "./src/main/resources/products.md"
    private const val PROMOTIONS_FILE_PATH = "./src/main/resources/promotions.md"
    private const val INVALID_PURCHASE_REQUEST = "올바르지 않은 구매 형식입니다."
    private const val PURCHASE_SCRIPT = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"
    private const val ASK_PROMOTION_APPLY = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니끼? (Y/N)"
    private const val ASK_PURCHASE_WITHOUT_PROMOTION ="현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"
    private const val INVALID_ANSWER = "Y 또는 N만 입력해야 합니다."
    private const val ASK_ANOTHER_PURCHASE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"

    private fun getFiieLines(path: String): List<String> {
        val file = File(path)
        val lines = file.readLines()
        return lines
    }

    fun getPromotions(): List<Promotion> {
        val promotions = mutableListOf<Promotion>()
        val lines = getFiieLines(PROMOTIONS_FILE_PATH)

        lines.drop(1).forEach { line ->
            val parts = line.split(",")
            promotions.add(
                Promotion(
                    parts[0],
                    parts[1].toInt(),
                    parts[2].toInt(),
                    LocalDate.parse(parts[3]),
                    LocalDate.parse(parts[4])
                )
            )
        }

        return promotions
    }

    fun getProducts(promotions: List<Promotion>): List<Product> {
        val products = mutableListOf<Product>()
        val lines = getFiieLines(PRODUCTS_FILE_PATH)

        lines.drop(1).forEach { line ->
            val parts = line.split(",")
            if (parts.size == 4) products.add(
                Product(
                    parts[0],
                    parts[1].toInt(),
                    parts[2].toInt(),
                    findPromotionByName(promotions, parts[3])
                )
            )
            else products.add(Product(parts[0], parts[1].toInt(), parts[2].toInt()))
        }

        return products
    }

    private fun findPromotionByName(promotions: List<Promotion>, name: String): Promotion? {
        return promotions.find { it.name == name }
    }

    fun getPurchase(inventory: Inventory): List<PurchaseItem> {
        println(PURCHASE_SCRIPT)
        val input = Console.readLine()
        val purchases = input.split(",")
        return validatePurchase(inventory, purchases)

    }

    private fun validatePurchase(
        inventory: Inventory,
        purchases: List<String>
    ): List<PurchaseItem> {
        val matchedResults = validatePurchaseFormat(purchases)
        return matchedResults.map { matchResult ->
            validatePurchaseItem(inventory, matchResult)
        }
    }

    private fun validatePurchaseFormat(purchases: List<String>): List<MatchResult> =
        purchases.map { purchase ->
            val regex = "\\[([a-zA-Z가-힣]+)-(\\d+)\\]".toRegex()
            val matchResult =
                regex.find(purchase) ?: throw IllegalArgumentException(getErrorMessage(INVALID_PURCHASE_REQUEST))
            matchResult
        }

    private fun validatePurchaseItem(inventory: Inventory, matchResult: MatchResult): PurchaseItem {
        val productName = matchResult.groupValues[1]
        inventory.containsProductName(productName)

        val quantity = matchResult.groupValues[2].toInt()
        inventory.checkQuantity(productName, quantity)

        return PurchaseItem(productName, quantity, inventory.getPriceByName(productName), PromotionStatus.convertToStatus(inventory, productName, quantity))
    }

    fun askToApplyPromotion(productName : String, getCount : Int) : Boolean {
        println(ASK_PROMOTION_APPLY.format(productName, getCount))
        val answer = Console.readLine()
        try {
            return when(answer) {
                "Y" -> true
                "N" -> false
                else -> throw IllegalArgumentException(getErrorMessage(INVALID_ANSWER))
            }
        } catch (e : Exception) {
            println(e.message)
            return askToApplyPromotion(productName, getCount)
        }
    }

    fun askToPurchaseWithoutPromotion(productName: String, nonPromotionalProductCount : Int) : Boolean {
        println(ASK_PURCHASE_WITHOUT_PROMOTION.format(productName, nonPromotionalProductCount ))
        val answer = Console.readLine()
        try {
            return when(answer) {
                "Y" -> true
                "N" -> false
                else -> throw IllegalArgumentException(getErrorMessage(INVALID_ANSWER))
            }
        } catch (e : Exception) {
            println(e.message)
            return askToPurchaseWithoutPromotion(productName, nonPromotionalProductCount)
        }
    }

    fun askToAnotherPurchase() : Boolean{
        println(ASK_ANOTHER_PURCHASE)
        val answer = Console.readLine()
        try {
            return when(answer) {
                "Y" -> true
                "N" -> false
                else -> throw IllegalArgumentException(getErrorMessage(INVALID_ANSWER))
            }
        } catch (e : Exception) {
            println(e.message)
            return askToAnotherPurchase()
        }
    }

}
