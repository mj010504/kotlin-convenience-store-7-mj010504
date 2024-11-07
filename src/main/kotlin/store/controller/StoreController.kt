package store.controller

import store.model.Inventory
import store.model.PurchaseItem
import store.utils.ErrorHandler.getErrorMessage
import store.view.InputView
import store.view.InputView.getPurchase
import store.view.InputView.getProducts
import store.view.InputView.getPromotions
import store.view.OutputView
import store.view.OutputView.printInventory

class StoreController {

    fun run() {
        val promotions = getPromotions()
        val products = getProducts(promotions)

        val inventory = Inventory(products)
        printInventory(inventory)

        val purchaseItems = getPurchase(inventory)
    }

    private fun getPurchaseItem(inventory: Inventory) : List<PurchaseItem> {
        try {
            return getPurchase(inventory)
        }
        catch (e : Exception) {
            println(e.message)
            return getPurchase(inventory)

        }

    }


}