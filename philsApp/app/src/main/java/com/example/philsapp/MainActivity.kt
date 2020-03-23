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
import com.example.philsapp.api.Database
import de.blox.graphview.*
import de.blox.graphview.energy.FruchtermanReingoldAlgorithm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_bar.*
import com.example.philsapp.api.Filter
import com.example.philsapp.api.FilterBy
import com.example.philsapp.api.Operator
import com.otaliastudios.zoom.ZoomApi.Companion.TYPE_ZOOM

class NodeInfo (val text: String,
                val type: String)
class MainActivity : AppCompatActivity() {
    val TAG = "kek"
    var nodeCount = 0

    object Nodes {
        var nodesInfo = arrayListOf<NodeInfo>()
        var nodes = arrayListOf<Node>()
    }
    object ForSearchResults {
        var selectedNode = 0
        lateinit var selectedPhil: Phil
        var fromActivity = "graph"
    }

    fun createGraph() {
        val graphView = findViewById<GraphView>(R.id.graph)
        // example tree
        graphView.setOnItemClickListener { parent, view, position, id ->
            ForSearchResults.selectedNode = position
            ForSearchResults.fromActivity = "graph"
            clearSearchResults()
            Log.d(TAG, ForSearchResults.selectedNode.toString())
            val myIntent = Intent(this, InfoCardActivity::class.java)
            startActivityForResult(myIntent, 4)
        }
        val graph = Graph()
        val db = Database(this)
        Nodes.nodesInfo = arrayListOf<NodeInfo>()
        Nodes.nodes = arrayListOf<Node>()
        val data = db.getAllPhilosophers(Filter(
            limit = if (FiltersActivity.Filters.topGT == 1) FiltersActivity.Filters.countGT else 20
        //    ,filter = arrayOf(FilterBy("birthDate", Operator.GT, FiltersActivity.Filters.yearStart),
          //      FilterBy("birthDate", Operator.LT, FiltersActivity.Filters.yearEnd))
        ))
        var philPos = 0
        var schoolPos = 0
        var ideaPos = 0
        data.forEach { it ->
            Nodes.nodesInfo.add(NodeInfo(it.name, "phil"))
            philPos = Nodes.nodesInfo.size - 1
            Nodes.nodes.add(Node(Nodes.nodesInfo[philPos].text))
            graph.addNode(Nodes.nodes[philPos])
            if (FiltersActivity.Filters.schools == 1) {
                it.schools.forEach {
                    val indexSchool = Nodes.nodesInfo.indexOf(NodeInfo(it.name, "school"))
                    if (indexSchool == -1) {
                        Nodes.nodesInfo.add(NodeInfo(it.name, "school"))
                        schoolPos = Nodes.nodesInfo.size - 1
                        Nodes.nodes.add(Node(Nodes.nodesInfo[schoolPos].text))
                        graph.addNode(Nodes.nodes[schoolPos])
                    } else {
                        schoolPos = indexSchool
                    }
                    graph.addEdge(Nodes.nodes[schoolPos], Nodes.nodes[philPos])
                }
            }
            if (FiltersActivity.Filters.meanings == 1) {
                it.notableIdeas.forEach {
                    val indexSchool = Nodes.nodesInfo.indexOf(NodeInfo(it.name, "meaning"))
                    if (indexSchool == -1) {
                        Nodes.nodesInfo.add(NodeInfo(it.name, "meaning"))
                        ideaPos = Nodes.nodesInfo.size - 1
                        Nodes.nodes.add(Node(Nodes.nodesInfo[ideaPos].text))
                        graph.addNode(Nodes.nodes[ideaPos])
                    } else {
                        ideaPos = indexSchool
                    }
                    graph.addEdge(Nodes.nodes[philPos], Nodes.nodes[ideaPos])
                }
            }
            if(Nodes.nodes.size > 50) {
                graphView.setMinZoom(10.0F, TYPE_ZOOM)
            } else if (Nodes.nodes.size > 30) {
                graphView.setMinZoom(5.0F, TYPE_ZOOM)

            }
        }
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
                when (Nodes.nodesInfo[position].type) {
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
            3 -> {
                Log.d(TAG, data.getStringExtra("search"))
                layoutForSearch.visibility = View.VISIBLE
                textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
                textSearchAllCount.text = FiltersActivity.SearchResults.countResults.toString()
            }
            4 -> Log.d(TAG, data.getStringExtra("data"))
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
            layoutForSearch.visibility = View.GONE
        }
        gotoLine.setOnClickListener{
            Log.d(TAG, "time line")
            val myIntent = Intent(this, TimeLineActivity::class.java)
            startActivityForResult(myIntent, 2)
            clearSearchResults()
        }
        buttonSearchLeft.setOnClickListener {
            when(FiltersActivity.SearchResults.selectedVariant) {
                0 -> FiltersActivity.SearchResults.selectedVariant = FiltersActivity.SearchResults.countResults - 1
                else -> FiltersActivity.SearchResults.selectedVariant--
            }
            textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
        }
        buttonSearchRight.setOnClickListener {
            when(FiltersActivity.SearchResults.selectedVariant) {
                FiltersActivity.SearchResults.countResults - 1 -> FiltersActivity.SearchResults.selectedVariant = 0
                else -> FiltersActivity.SearchResults.selectedVariant++
            }
            textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
        }
        buttonSearchClose.setOnClickListener {
            clearSearchResults()
        }
        Log.d(TAG,"startQWQW")
        createGraph()

    }
    fun clearSearchResults() {
        layoutForSearch.visibility = View.GONE
        FiltersActivity.SearchResults.wordForSearch = ""
        FiltersActivity.SearchResults.countResults = 0
        FiltersActivity.SearchResults.selectedVariant = 0
        FiltersActivity.SearchResults.listVariants = arrayListOf<Any>()
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

