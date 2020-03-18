package com.example.philsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.blox.graphview.*
import de.blox.graphview.energy.FruchtermanReingoldAlgorithm
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val TAG = "kek"
    var nodeCount = 1
    fun showFilters() {
        Log.d(TAG, "filters")
        val myIntent = Intent(this, FiltersActivity::class.java)
        startActivityForResult(myIntent, 1)
    }

    fun showGraph() {
        Log.d(TAG, "current")
    }

    fun showTimeLine() {
        Log.d(TAG, "time line")
        val myIntent = Intent(this, TimeLineActivity::class.java)
        startActivityForResult(myIntent, 3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when (requestCode) {
            1 -> Log.d(TAG, "код 1")
            2 -> Log.d(TAG, "код 2")
            3 -> Log.d(TAG, "код 3")
            else -> Log.d(TAG, "другой")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navbar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_filters -> showFilters()
                R.id.nav_graph -> showGraph()
                R.id.nav_timeline -> showTimeLine()
                else -> {
                    Log.d("kek", "Назад")
                }
            }
            true

        }
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
        val h = Node(getNodeText())
        val nodes = arrayListOf<Node>()
        for (n in 1..100) {
            nodes.add(Node(getNodeText()))
        }
        for (n in 1..100) {
            graph.addEdge(nodes[(0..99).random()], nodes[(0..99).random()])
        }
        graph.addEdge(a, b)
        graph.addEdge(a, c)
        graph.addEdge(a, d)
        graph.addEdge(c, e)
        graph.addEdge(d, f)
        graph.addEdge(f, c)
        graph.addEdge(g, c)
        graph.addEdge(h, g)
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
                (viewHolder as SimpleViewHolder).textView.setText(data.toString())
            }
        }
        graphView.adapter = adapter
        // set the algorithm here
        adapter.setAlgorithm(FruchtermanReingoldAlgorithm(1000))
    }

    private fun getNodeText(): String {
        return "Node " + nodeCount++
    }
}
internal class SimpleViewHolder(itemView: View) : ViewHolder(itemView) {
    var textView: TextView

    init {
        textView = itemView.findViewById(R.id.nodeTextView)
    }
}

