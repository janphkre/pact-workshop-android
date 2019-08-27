package au.com.dius.pactconsumer.presentation

import android.app.Activity
import android.os.Bundle
import au.com.dius.pactconsumer.R
import au.com.dius.pactconsumer.apiclient.AnimalsProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.view.View
import au.com.dius.pactconsumer.model.AnimalCollection
import au.com.dius.pactconsumer.util.AndroidSchedulers
import rx.Subscriber
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*


open class MainActivity: Activity() {

    private val hostUrl = "https://api.testserver.com/"
    private lateinit var dataProvider: AnimalsProvider

    private lateinit var loadingView: View
    private lateinit var errorView: TextView
    private lateinit var recyclerView: RecyclerView

    private val subscriptions = CompositeSubscription()

    protected open fun getDataProvider(): AnimalsProvider {
        return AnimalsProvider.getInstance(hostUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animals)

        dataProvider = getDataProvider()

        initView()
        loadData()
    }

    override fun onDestroy() {
        subscriptions.clear()
        super.onDestroy()
    }

    private fun initView() {
        loadingView = findViewById(R.id.view_loading)
        errorView = findViewById(R.id.txt_error)
        recyclerView = findViewById(R.id.view_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    private fun showError(errorMessage: String) {
        errorView.text = errorMessage
        errorView.visibility = View.VISIBLE
    }

    private fun hideError() {
        errorView.visibility = View.GONE
    }

    private fun loadData() {
        showLoading()
        subscriptions.add(dataProvider.getAnimals(Calendar.getInstance())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread)
            .subscribe(object: Subscriber<AnimalCollection>() {
                override fun onNext(animals: AnimalCollection) {
                    if(animals.animals.isEmpty()) {
                        showError(getString(R.string.empty_message))
                    } else {
                        hideError()
                        recyclerView.adapter = AnimalsAdapter(animals.animals)
                    }
                }

                override fun onError(e: Throwable?) {
                    showError(getString(R.string.error_message))
                    hideLoading()
                }

                override fun onCompleted() { }
            }))
    }
}