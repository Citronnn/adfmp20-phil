package com.example.phils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import de.blox.graphview.BaseGraphAdapter
import de.blox.graphview.Graph
import de.blox.graphview.Node
import de.blox.graphview.ViewHolder
import de.blox.graphview.energy.FruchtermanReingoldAlgorithm
import kotlinx.android.synthetic.main.fragment_graph.*


class GraphFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val graph = Graph()
        val node1 = Node(getNodeText())
        val node2 = Node(getNodeText())
        val node3 = Node(getNodeText())
        graph.addEdge(node1, node2)
        graph.addEdge(node1, node3)
        // you can set the graph via the constructor or use the adapter.setGraph(Graph) method
        val adapter: BaseGraphAdapter<ViewHolder> = object : BaseGraphAdapter<ViewHolder>(graph) {
            @NonNull
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.node, parent, false)
                return SimpleViewHolder(view)
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, data: Any, position: Int) {
                (viewHolder as SimpleViewHolder).textView.text = data.toString()
            }

            inner class SimpleViewHolder(itemView: View) :
                ViewHolder(itemView) {
                var textView: TextView = itemView.findViewById(R.id.textView)

            }
        }
        adapter.graph = graph
        adapter.algorithm = FruchtermanReingoldAlgorithm(1000)
        graph_view?.adapter = adapter
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    private fun getNodeText(): String? {
        return "Node "
    }
}
