package store.view

import camp.nextstep.edu.missionutils.Console
import store.model.Product
import store.model.Promotion
import store.utils.ErrorHandler.inputErrorHandler
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

object InputView {

    private const val PRODUCTS_FILE = "./src/main/resources/products.md"
    private const val PROMOTIONS_FILE = "./src/main/resources/promotions.md"
    private const val FILE_READ_ERROR = "파일을 읽는 중 오류가 발생했습니다"

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

    fun getPurchaseProductAndQuantity() {
        Console.readLine()
    }
}