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
import com.example.philsapp.api.*
import de.blox.graphview.*
import de.blox.graphview.energy.FruchtermanReingoldAlgorithm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_bar.*
import com.otaliastudios.zoom.ZoomApi.Companion.TYPE_ZOOM

class NodeInfo (val text: String,
                val type: String,
                val id: Int)
class MainActivity : AppCompatActivity() {
    val TAG = "kek"

    object Nodes {
        var nodesInfo = arrayListOf<NodeInfo>()
        var nodes = arrayListOf<Node>()
    }
    object onClickResults {
        var selectedId = 0
        var selectedType = "phil"
        var selectedName = ""
    }

    fun createGraph(forSearch:Boolean) {
        val graphView = findViewById<GraphView>(R.id.graph)
        // example tree
        val graph = Graph()
        val db = Database(this)
        Nodes.nodesInfo = arrayListOf<NodeInfo>()
        Nodes.nodes = arrayListOf<Node>()
        if (forSearch) {
            var current = FiltersActivity.SearchResults.selectedVariant
            var typeForSearch = "phil"
            if (current > FiltersActivity.SearchResults.listPhils.size - 1) {
                typeForSearch = "school"
                current -= FiltersActivity.SearchResults.listPhils.size
                if (current > FiltersActivity.SearchResults.listSchools.size - 1) {
                    typeForSearch = "meaning"
                    current -= FiltersActivity.SearchResults.listSchools.size
                    if (current > FiltersActivity.SearchResults.listIdeas.size - 1) {
                        current -= FiltersActivity.SearchResults.listIdeas.size
                        typeForSearch = "age"
                    }
                }
            }
            when (typeForSearch) {
                "phil" -> {
                    val data = FiltersActivity.SearchResults.listPhils[current]
                    var philPos = 0
                    var schoolPos = 0
                    var ideaPos = 0
                    Nodes.nodesInfo.add(NodeInfo(data.name, "phil", data.wikiPageId))
                    philPos = Nodes.nodesInfo.size - 1
                    Nodes.nodes.add(Node(Nodes.nodesInfo[philPos].text))
                    graph.addNode(Nodes.nodes[philPos])
                    if (FiltersActivity.Filters.schools == 1) {
                        data.schools.forEach {
                            Nodes.nodesInfo.add(NodeInfo(it.name, "school",0))
                            schoolPos = Nodes.nodesInfo.size - 1
                            Nodes.nodes.add(Node(Nodes.nodesInfo[schoolPos].text))
                            graph.addNode(Nodes.nodes[schoolPos])
                            graph.addEdge(Nodes.nodes[schoolPos], Nodes.nodes[philPos])
                        }
                    }
                    if (FiltersActivity.Filters.meanings == 1) {
                        data.notableIdeas.forEach {
                            Nodes.nodesInfo.add(NodeInfo(it.name, "meaning",0))
                            ideaPos = Nodes.nodesInfo.size - 1
                            Nodes.nodes.add(Node(Nodes.nodesInfo[ideaPos].text))
                            graph.addNode(Nodes.nodes[ideaPos])
                            graph.addEdge(Nodes.nodes[philPos], Nodes.nodes[ideaPos])
                        }
                    }
                }
                "school" -> {
                    val data = FiltersActivity.SearchResults.listSchools[current]
                    var philPos = 0
                    var schoolPos = 0
                    Nodes.nodesInfo.add(NodeInfo(data.name, "school",0))
                    Nodes.nodes.add(Node(Nodes.nodesInfo[schoolPos].text))
                    graph.addNode(Nodes.nodes[schoolPos])
                    data.philosophers.forEach {
                        Nodes.nodesInfo.add(NodeInfo(it.name, "phil", it.wikiPageId))
                        philPos = Nodes.nodesInfo.size - 1
                        Nodes.nodes.add(Node(Nodes.nodesInfo[philPos].text))
                        graph.addNode(Nodes.nodes[philPos])
                        graph.addEdge(Nodes.nodes[schoolPos], Nodes.nodes[philPos])
                    }
                }
                "meaning" -> {
                    val data = FiltersActivity.SearchResults.listIdeas[current]
                    Nodes.nodesInfo.add(NodeInfo(data.name, "meaning",0))
                    Nodes.nodes.add(Node(Nodes.nodesInfo[0].text))
                    graph.addNode(Nodes.nodes[Nodes.nodes.size - 1])
                }
                "age" -> {
                    val data = FiltersActivity.SearchResults.listEras[current]
                    Nodes.nodesInfo.add(NodeInfo(data.name, "age",0))
                    Nodes.nodes.add(Node(Nodes.nodesInfo[0].text))
                    graph.addNode(Nodes.nodes[Nodes.nodes.size - 1])
                }
            }
        } else {
            val data = db.getAllPhilosophers(
                Filter(
                    limit = if (FiltersActivity.Filters.topGT == 1) FiltersActivity.Filters.countGT else 20,
                    filter = arrayOf(FilterBy("birthDate", Operator.GT, dateToJd(FiltersActivity.Filters.yearStart)),
                          FilterBy("birthDate", Operator.LT, dateToJd(FiltersActivity.Filters.yearEnd)))
                )
            )
            var philPos = 0
            var schoolPos = 0
            var ideaPos = 0
            data.forEach { it ->
                Nodes.nodesInfo.add(NodeInfo(it.name, "phil",it.wikiPageId))
                philPos = Nodes.nodesInfo.size - 1
                Nodes.nodes.add(Node(Nodes.nodesInfo[philPos].text))
                graph.addNode(Nodes.nodes[philPos])
                if (FiltersActivity.Filters.schools == 1) {
                    it.schools.forEach {
                        val school = Nodes.nodes.find {nI: Node ->
                            nI.data == it.name
                        }
                        if (school == null) {
                            Nodes.nodesInfo.add(NodeInfo(it.name, "school",0))
                            schoolPos = Nodes.nodesInfo.size - 1
                            Nodes.nodes.add(Node(Nodes.nodesInfo[schoolPos].text))
                            graph.addNode(Nodes.nodes[schoolPos])
                            graph.addEdge(Nodes.nodes[schoolPos], Nodes.nodes[philPos])
                        } else {
                            graph.addEdge(school, Nodes.nodes[philPos])
                        }
                    }
                }
                if (FiltersActivity.Filters.meanings == 1) {
                    it.notableIdeas.forEach {
                        val idea = Nodes.nodes.find {nI: Node ->
                            nI.data == it.name
                        }
                        if (idea == null) {
                            Nodes.nodesInfo.add(NodeInfo(it.name, "meaning",0))
                            ideaPos = Nodes.nodesInfo.size - 1
                            Nodes.nodes.add(Node(Nodes.nodesInfo[ideaPos].text))
                            graph.addNode(Nodes.nodes[ideaPos])
                            graph.addEdge(Nodes.nodes[philPos], Nodes.nodes[ideaPos])
                        } else {
                            graph.addEdge(Nodes.nodes[philPos], idea)
                        }
                    }
                }
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
                    "age" -> {
                        (viewHolder as SimpleViewHolder).formView.setBackgroundColor(Color.parseColor("#f49393"))
                    }
                }
                (viewHolder as SimpleViewHolder).textView.setText(data.toString())
            }
        }
        adapter.graph = graph
        graphView.adapter = adapter
        graphView.setOnItemClickListener { parent, view, position, id ->
            onClickResults.selectedId = Nodes.nodesInfo[position].id
            onClickResults.selectedType = Nodes.nodesInfo[position].type
            onClickResults.selectedName = Nodes.nodesInfo[position].text
            clearSearchResults()
            val myIntent = Intent(this, InfoCardActivity::class.java)
            startActivityForResult(myIntent, 4)
        }
        // set the algorithm here
        adapter.setAlgorithm(FruchtermanReingoldAlgorithm(1000))

        graphView.setMinZoom(1.0F, TYPE_ZOOM)
        if(Nodes.nodes.size > 50) {
            graphView.setMinZoom(10.0F, TYPE_ZOOM)
        } else if (Nodes.nodes.size > 20) {
            graphView.setMinZoom(5.0F, TYPE_ZOOM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        var forSearch = false
        when (resultCode) {
            1 -> Log.d(TAG, data.getStringExtra("filters"))
            2 -> Log.d(TAG, data.getStringExtra("filters"))
            3 -> {
                Log.d(TAG, data.getStringExtra("search"))
                layoutForSearch.visibility = View.VISIBLE
                textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
                textSearchAllCount.text = FiltersActivity.SearchResults.countResults.toString()
                forSearch = true
            }
            4 -> Log.d(TAG, data.getStringExtra("data"))
            else -> Log.d(TAG, "другой")
        }
        createGraph(forSearch)
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
            createGraph(true)
        }
        buttonSearchRight.setOnClickListener {
            when(FiltersActivity.SearchResults.selectedVariant) {
                FiltersActivity.SearchResults.countResults - 1 -> FiltersActivity.SearchResults.selectedVariant = 0
                else -> FiltersActivity.SearchResults.selectedVariant++
            }
            createGraph(true)
            textSearchCurrentId.text = (FiltersActivity.SearchResults.selectedVariant + 1).toString()
        }
        buttonSearchClose.setOnClickListener {
            clearSearchResults()
            createGraph(false)
        }
        Log.d(TAG,"startQWQW")
        createGraph(false)

    }
    fun clearSearchResults() {
        layoutForSearch.visibility = View.GONE
        FiltersActivity.SearchResults.wordForSearch = ""
        FiltersActivity.SearchResults.countResults = 0
        FiltersActivity.SearchResults.selectedVariant = 0
        FiltersActivity.SearchResults.listPhils = arrayListOf()
        FiltersActivity.SearchResults.listSchools = arrayListOf()
        FiltersActivity.SearchResults.listIdeas = arrayListOf()
        FiltersActivity.SearchResults.listEras= arrayListOf()
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

