package store.controller

import store.model.Inventory
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

        InputView.getPurchase(inventory)
    }

    private fun getPurchase(inventory: Inventory) {
        InputView.getPurchase(inventory)
    }


}