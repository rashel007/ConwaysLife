package com.rashel.conwayslife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.rashel.conwayslife.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels()

    val adapter = MainAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModelObserver()
        initView()
    }


    private fun initViewModelObserver() {
        viewModel.uiGeneration.observe(this) {
            Log.d("IsGenerationChanged", "isCHanged : ${viewModel.uiState.value}")
            updateAdapter(viewModel.getMatrixList(it))

            if (viewModel.isRunning)
                viewModel.checkLivingCondition(it)
        }

    }

    lateinit var dataMain: ArrayList<ArrayList<Person>>
    private fun initView() {


        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 10)

        viewModel.getMatrix {
            dataMain = it
            updateAdapter(viewModel.getMatrixList(it))
            if (viewModel.isRunning)
                viewModel.checkLivingCondition(it)

        }

        binding.btnPlay.setOnClickListener {
            viewModel.isRunning = !viewModel.isRunning
            adapter.updateGameRunning(viewModel.isRunning)
            if (viewModel.isRunning) {
                binding.btnPlay.text = "Stop"
                viewModel.checkLivingCondition(dataMain)
            } else {
                binding.btnPlay.text = "Start"
                viewModel.getMatrix {
                    dataMain = it
                    updateAdapter(viewModel.getMatrixList(it))
                }
            }
        }


    }

    private fun updateAdapter(items: ArrayList<Person>) {
        adapter.updateData(items, object : MainAdapter.OnItemClickListener {
            override fun onItemClick(item: Person) {
                var xPos = 0
                var yPos = 0
                if (item.id <= 10) {
                    yPos = item.id - 1
                } else {
                    val id = item.id - 1
                    xPos = (id / 10)
                    yPos = (id % 10)
                }
                dataMain[xPos][yPos].isAlive = item.isAlive
                Log.d("checkLivingCondition", "${Gson().toJson(dataMain)}")
            }
        })
    }
}