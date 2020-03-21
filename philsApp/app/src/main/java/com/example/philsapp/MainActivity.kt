package com.example.philsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import de.blox.graphview.*
import de.blox.graphview.energy.FruchtermanReingoldAlgorithm
import kotlinx.android.synthetic.main.activity_main.*

class NodeInfo (val text: String,
                val type: String)
class MainActivity : AppCompatActivity() {
    val TAG = "kek"
    var nodeCount = 0
    val nodes = listOf(
        NodeInfo("Философ", "phil"),
        NodeInfo("Философ2", "school"),
        NodeInfo("Школа1", "school"),
        NodeInfo("Понятие1", "meaning"),
        NodeInfo("Понятие2", "meaning"),
        NodeInfo("Понятие3", "meaning"),
        NodeInfo("Понятие4", "meaning")
    )

    fun createGraph() {
        val graphView = findViewById<GraphView>(R.id.graph)
        // example tree
        val graph = Graph()
        val a = Node(getNodeText())
        val b = Node(getNodeText())
        val c = Node(getNodeText())
        val d = Node(getNodeText())
        val e = Node(getNodeText())
        val f = Node(getNodeText())
        val g = Node(getNodeText())
        graph.addEdge(c, a)
        graph.addEdge(c, b)
        graph.addEdge(a, d)
        graph.addEdge(a, e)
        graph.addEdge(a, f)
        graph.addEdge(a, g)
        // you can set the graph via the constructor or use the adapter.setGraph(Graph) method
        val adapter: BaseGraphAdapter<ViewHolder?> = object : BaseGraphAdapter<ViewHolder?>(graph) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.node, parent, false)
                return SimpleViewHolder(view)
            }

            override fun onBindViewHolder(
                viewHolder: ViewHolder?,
                data: Any?,
                position: Int
            ) {
                Log.d(TAG, position.toString())
                Log.d(TAG, nodes[position].type)
                when (nodes[position].type) {
                    "phil" -> {
                        (viewHolder as SimpleViewHolder).formView.setBackgroundColor(Color.YELLOW)
                        (viewHolder as SimpleViewHolder).textView.setTextColor(Color.BLACK)
                    }
                    "school" -> {
                        (viewHolder as SimpleViewHolder).formView.setBackgroundColor(Color.GREEN)
                        (viewHolder as SimpleViewHolder).textView.setTextColor(Color.BLACK)
                    }
                    "meaning" -> {
                        (viewHolder as SimpleViewHolder).formView.setBackgroundColor(Color.BLUE)
                    }
                }
                (viewHolder as SimpleViewHolder).textView.setText(data.toString())
            }
        }
        graphView.adapter = adapter
        // set the algorithm here
        adapter.setAlgorithm(FruchtermanReingoldAlgorithm(1000))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        nodeCount = 0
        createGraph()
        when (resultCode) {
            1 -> Log.d(TAG, data.getStringExtra("filters"))
            2 -> Log.d(TAG, data.getStringExtra("filters"))
            3 -> Log.d(TAG, data.getStringExtra("search"))
            else -> Log.d(TAG, "другой")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        filters.setOnClickListener{
            Log.d(TAG, "filters")
            val myIntent = Intent(this, FiltersActivity::class.java)
            startActivityForResult(myIntent, 1)
        }
        gotoLine.setOnClickListener{
            Log.d(TAG, "time line")
            Log.d(TAG, FiltersActivity.Filters.phils.toString())
            val myIntent = Intent(this, TimeLineActivity::class.java)
            startActivityForResult(myIntent, 2)
        }
        Log.d(TAG,"startQWQW")
        createGraph()
    }

    private fun getNodeText(): String {
        nodeCount++
        return nodes[nodeCount-1].text
    }
}
internal class SimpleViewHolder(itemView: View) : ViewHolder(itemView) {
    var textView: TextView
    var formView: CardView

    init {
        textView = itemView.findViewById(R.id.nodeTextView)
        formView = itemView.findViewById(R.id.card_view)
    }
}

