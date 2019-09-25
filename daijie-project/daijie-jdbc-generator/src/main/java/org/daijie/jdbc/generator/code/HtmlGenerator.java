package org.daijie.jdbc.generator.code;

import org.daijie.jdbc.generator.executor.Generator;
import org.daijie.jdbc.matedata.ColumnMateData;
import org.daijie.jdbc.matedata.TableMateData;

import java.util.List;

/**
 * JAVA类属性代码生成器
 * @author daijie
 * @since 2019/9/16
 */
public class HtmlGenerator implements Generator {

    /**
     * 表元数据
     */
    private List<TableMateData> tableMateDatas;

    public HtmlGenerator(List<TableMateData> tableMateDatas) {
        this.tableMateDatas = tableMateDatas;
    }

    @Override
    public Object execute() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<!DOCTYPE html>                                                                 \n");
        buffer.append("<html>                                                                          \n");
        buffer.append("	<head>                                                                         \n");
        buffer.append("		<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">          \n");
        buffer.append("		<title>数据结构</title>                                                      \n");
        buffer.append("		<style>                                                                      \n");
        buffer.append("			.body{                                                                     \n");
        buffer.append("				width: 95%;                                                          \n");
        buffer.append("				height: 600px;                                                          \n");
        buffer.append("				padding: 10px 20px 10px 20px;                                            \n");
        buffer.append("				border: 2px solid #000;                                                  \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.body>div{                                                                 \n");
        buffer.append("				float: left;                                                             \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-left{                                                               \n");
        buffer.append("				width: 32%;                                                            \n");
        buffer.append("				height: 100%;                                                            \n");
        buffer.append("				font-size: 14px;                                                         \n");
        buffer.append("				border-right: 2px solid #000;                                            \n");
        buffer.append("				overflow-x :hidden;                                                       \n");
        buffer.append("				overflow-y :auto;                                                       \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-left>input{                                                            \n");
        buffer.append("				height: 20px;                                                            \n");
        buffer.append("				font-size: 16px;                                                            \n");
        buffer.append("				width: 80%;                                                            \n");
        buffer.append("				margin-bottom: 5px;                                                            \n");
        buffer.append("			}                                                            \n");
        buffer.append("			.table-left>ul{                                                            \n");
        buffer.append("				list-style-type: none;                                                   \n");
        buffer.append("				padding-left: 0;                                                         \n");
        buffer.append("				margin-top: 0;                                                         \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-left>ul>li.background{                                                                          \n");
        buffer.append("				background: #888;                                                                          \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-left>ul>li:hover, .table-left>ul>li:focus{                          \n");
        buffer.append("				cursor:pointer;                                                          \n");
        buffer.append("				background-color:#A7C942;                                                \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-right{                                                              \n");
        buffer.append("				width: 66%;                                                           \n");
        buffer.append("				font-size: 14px;                                                         \n");
        buffer.append("				padding: 2px 0 0 10px;                                                   \n");
        buffer.append("				overflow:scroll;                                                   \n");
        buffer.append("				height: 100%;                                                   \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-right>table{                                                        \n");
        buffer.append("				font-family:\"Trebuchet MS\", Arial, Helvetica, sans-serif;                \n");
        buffer.append("				width:100%;                                                              \n");
        buffer.append("  				border-collapse:collapse;                                              \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-right>table th, .table-right>table td{                              \n");
        buffer.append("				font-size:1em;                                                           \n");
        buffer.append("				border:1px solid #98bf21;                                                \n");
        buffer.append("				padding:3px 7px 2px 7px;                                                 \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-right>table th{                                                     \n");
        buffer.append("				font-size:1.1em;                                                         \n");
        buffer.append("				text-align:left;                                                         \n");
        buffer.append("				padding-top:5px;                                                         \n");
        buffer.append("				padding-bottom:4px;                                                      \n");
        buffer.append("				background-color:#A7C942;                                                \n");
        buffer.append("				color:#ffffff;                                                           \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-left>ul>li.show, .table-right>table.open{                                                   \n");
        buffer.append("				display: '';                                                             \n");
        buffer.append("			}                                                                          \n");
        buffer.append("			.table-left>ul>li.hide, .table-right>table.close{                                                  \n");
        buffer.append("				display: none;                                                           \n");
        buffer.append("			}                                                                          \n");
        buffer.append("		</style>                                                                     \n");
        buffer.append("		<script type=\"text/javascript\">                                              \n");
        buffer.append("			window.onload = function(){                                                \n");
        buffer.append("				var menu = document.getElementById(\"table-menu\");                        \n");
        buffer.append("				menu.addEventListener(\"click\",function(event){                           \n");
        buffer.append("					var nodes = document.getElementsByClassName(\"background\");                    \n");
        buffer.append("					for(var i = 0; i < nodes.length; i ++){                                        \n");
        buffer.append("						nodes[i].className = nodes[i].className.replace(\"background\",\"\");        \n");
        buffer.append("					}                                                                        \n");
        buffer.append("					event.target.className = \"background\";                                   \n");
        buffer.append("					var open = document.getElementsByClassName(\"open\");                    \n");
        buffer.append("					for(var i = 0; i < open.length; i ++){                                  \n");
        buffer.append("						open[i].className = open[i].className.replace(\"open\",\"close\");       \n");
        buffer.append("					}                                                                        \n");
        buffer.append("					var element =  document.getElementsByClassName(event.target.innerHTML);  \n");
        buffer.append("					for(var i = 0; i < element.length; i ++){                                   \n");
        buffer.append("						element[i].className = element[i].className.replace(\"close\",\"open\"); \n");
        buffer.append("					}                                                                            \n");
        buffer.append("				});                                                                             \n");
        buffer.append("				var doctables = menu.childNodes;                                                  \n");
        buffer.append("				var search = document.getElementById(\"search\");                                 \n");
        buffer.append("				search.onkeyup = function(event){                                                    \n");
        buffer.append("				    var value = search.value;                                                         \n");
        buffer.append("				    for(var i = 0; i < doctables.length; i ++){                                       \n");
        buffer.append("				        if(value.trim() == \"\" || doctables[i].innerHTML.indexOf(value) >= 0){       \n");
        buffer.append("				         	doctables[i].className = \"show\"                                       \n");
        buffer.append("				        }else{                                                                         \n");
        buffer.append("				        	doctables[i].className = \"hide\"                                       \n");
        buffer.append("				        }                                                                        \n");
        buffer.append("				    }                                                                          \n");
        buffer.append("				}                                                                      \n");
        buffer.append("			}                                                                          \n");
        buffer.append("		</script>                                                                    \n");
        buffer.append("	</head>                                                                        \n");
        buffer.append("	<body>                                                                         \n");
        buffer.append("		<div class=\"body\">                                                           \n");
        buffer.append("			<div class=\"table-left\">                                                   \n");
        buffer.append("				<input id=\"search\" type=\"text\" placeholder=\"搜索\"/>                               \n");
        buffer.append("     <ul id=\"table-menu\">");
        for (TableMateData tableMateData : this.tableMateDatas) {
            buffer.append("<li>" + tableMateData.getName() + "(" + tableMateData.getRemarks() + ")" + "</li>");
        }
        buffer.append("     </ul>\n");
        buffer.append("		</div>                                                                     \n");
        buffer.append("			<div class=\"table-right\">                                                  \n");
        for (TableMateData tableMateData : this.tableMateDatas) {
            buffer.append("				<table class=\"close "+ tableMateData.getName() + "(" + tableMateData.getRemarks() + ")" +"\">                                                \n");
            buffer.append("					<thead>                                                                \n");
            buffer.append("						<tr>                                                                 \n");
            buffer.append("							<th width=\"20%\">名称</th>                                          \n");
            buffer.append("							<th width=\"70%\">注释</th>                                          \n");
            buffer.append("							<th width=\"10%\">类型</th>                                          \n");
            buffer.append("						</tr>                                                                \n");
            buffer.append("					</thead>                                                               \n");
            buffer.append("					<tbody>                                                                \n");
            for (String key : tableMateData.getColumns().keySet()) {
                ColumnMateData columnMateData = tableMateData.getColumn(key);
                buffer.append("						<tr>                                                                 \n");
                buffer.append("							<td>" + columnMateData.getName() + "</td>                                                    \n");
                buffer.append("							<td>" + columnMateData.getRemarks() + "</td>                                                    \n");
                buffer.append("							<td>" + columnMateData.getColumnType() + "</td>                                                   \n");
                buffer.append("						</tr>                                                                \n");
            }
            buffer.append("					</tbody>                                                               \n");
            buffer.append("				</table>                                                                 \n");
        }
        buffer.append("			</div>                                                                     \n");
        buffer.append("		</div>                                                                       \n");
        buffer.append("		                                                                             \n");
        buffer.append("	</body>                                                                        \n");
        buffer.append("</html>                                                                         \n");
        return buffer.toString();
    }
}
