package org.runite.jagex;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DumpingTools {
    public static boolean alreadyRan = false;

    public static final int NUMBER_OF_METHODS = 2065;
    /**
     * Put all code you want to run once for debugging purposes here.
     */
    public static void RunOnceAfterStartup() {
        if (alreadyRan)
            return;
        alreadyRan = true;
        // Uncomment one of these to do it.

        //PrintAllValidGameMethods();
        //GetAllMainMethods("mainmeths.txt");
        //DumpOpcodesToTextFile("method2ops.txt", 2); // Good for analyzing program structure.
        //DumpToRuneStarCompatible("binaryScripts"); // Requires making the folder first. Probably the most useful one.
    }

    public static void DumpOpcodesToTextFile(String outputFile, int methodID) {
        try {
            AssembledMethod a = ItemDefinition.getMethodByID(methodID);
            FileWriter myWriter = new FileWriter(outputFile);
            myWriter.write("Opcodes for Method" + methodID + ":\n");
            for (int opcode : a.assemblyInstructions) {
                myWriter.write(opcode + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Cannot write to file " + outputFile);
        }
    }

    /**
     * Dumps all scripts to runestar compatible format.
     */
    public static void DumpToRuneStarCompatible(String outputFolder) {
        for (int i = 0; i < NUMBER_OF_METHODS; i++) {
            try {
                AssembledMethod am = ItemDefinition.getMethodByID(i);
                if (am == null) {
                    continue;
                }
                FileOutputStream fileOut = new FileOutputStream(outputFolder + "/" + i);
                ByteBuffer fileData = ByteBuffer.allocate(65536 * 4);
                fileData.put((byte) 0);
                Short[] ops = new Short[am.assemblyInstructions.length];
                for (int j = 0; j < ops.length; j++) {
                    int s = am.assemblyInstructions[j];
                    fileData.putShort((short) s);
                    if (s == CS2AsmOpcodes.RETURN.getOp() || s == 38 || s == 39 || s >= 100) {
                        fileData.put((byte) am.instructionOperands[j]);
                    } else if (s == CS2AsmOpcodes.PUSH_STR.getOp()) {
                        fileData.put(am.stringInstructionOperands[j].buffer);
                        fileData.put((byte) 0);
                    } else {
                        fileData.putInt(am.instructionOperands[j]);
                    }
                }
                String s = "Generated by 2009scape. https://github.com/2009scape/2009scape";
                fileData.put(s.getBytes());
                fileData.putInt(am.assemblyInstructions.length);
                fileData.putShort((short) am.numberOfIntsToCopy);
                fileData.putShort((short) am.numberOfRSStringsToCopy);
                fileData.putShort((short) am.numberOfIntArguments);
                fileData.putShort((short) am.numberOfStringArguments);
                int offset = fileData.position();
                if (am.switchHashTable != null) {
                    fileData.put((byte) am.switchHashTable.length);
                    for (Class130 sw : am.switchHashTable) {
                        fileData.putShort((short) sw.aClass3Array1697.length);
                        for (Class3 singleSwitch : sw.aClass3Array1697) {
                            int l1 = (int) (singleSwitch.aLong71);
                            int l2 = (int) (singleSwitch.aLong71 >> 32);
                            fileData.putInt(l1);
                            fileData.putInt(l2);
                        }
                    }
                }
                int end = fileData.position();
                // Finally, put the offset of the data
                fileData.putShort((short) (end - offset));
                // And write binary file
                fileOut.write(fileData.array(), 0, fileData.position());
            } catch (IOException ioException) {
                System.out.println("Cannot write to file because " + ioException.getMessage());
            }
        }
    }

    /**
     * All script methods called by the runAssembledMethod function are "main" methods. that is, they
     * do not get called by other methods. The following function generates
     * a list of main methods.
     *
     * So a method is either a "main" method or a "helper" method, and "helper" methods are never called
     * To generate the list of main methods the following function can be used
     */

    // Here is the generated list:
    public static final int[] MAIN_METHODS = {6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 22, 23, 25, 26, 27, 28, 29, 30, 32, 33, 34, 35, 36, 38, 40, 43, 44, 45, 48, 49, 50, 52, 53, 54, 58, 59, 60, 61, 63, 64, 65, 66, 67, 68, 69, 70, 73, 80, 81, 82, 83, 86, 87, 88, 92, 93, 94, 95, 96, 97, 99, 100, 101, 103, 104, 105, 106, 108, 109, 110, 112, 113, 114, 115, 116, 117, 118, 119, 120, 122, 123, 124, 126, 127, 128, 130, 131, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 149, 150, 151, 152, 155, 156, 158, 159, 162, 163, 164, 165, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 182, 186, 187, 188, 189, 190, 191, 194, 195, 196, 199, 200, 201, 204, 205, 206, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 396, 397, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444, 445, 446, 447, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 458, 459, 460, 461, 462, 463, 464, 465, 466, 467, 468, 469, 470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 497, 498, 499, 500, 501, 502, 507, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 521, 522, 524, 525, 526, 527, 529, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 544, 545, 546, 548, 549, 550, 551, 552, 553, 554, 555, 556, 557, 560, 561, 562, 563, 564, 565, 566, 567, 568, 570, 571, 572, 574, 575, 579, 580, 581, 583, 585, 586, 587, 588, 589, 590, 591, 592, 595, 596, 597, 598, 599, 600, 601, 603, 604, 605, 606, 607, 608, 610, 611, 612, 613, 614, 615, 616, 617, 618, 619, 620, 629, 631, 633, 634, 635, 637, 639, 641, 643, 645, 647, 648, 649, 650, 653, 654, 655, 656, 663, 664, 665, 669, 675, 676, 677, 678, 681, 685, 687, 688, 690, 691, 692, 694, 695, 696, 697, 698, 699, 700, 701, 702, 703, 707, 708, 709, 710, 712, 714, 715, 716, 717, 718, 720, 721, 724, 725, 726, 727, 728, 729, 731, 732, 733, 735, 740, 741, 742, 743, 745, 746, 747, 748, 749, 751, 752, 753, 754, 755, 756, 757, 758, 763, 764, 769, 770, 772, 774, 776, 777, 778, 779, 782, 783, 784, 786, 787, 789, 790, 791, 792, 796, 797, 800, 801, 803, 808, 809, 811, 812, 815, 816, 819, 820, 825, 826, 827, 828, 829, 830, 832, 833, 834, 835, 836, 838, 839, 840, 842, 843, 844, 845, 847, 848, 856, 857, 858, 859, 860, 862, 863, 864, 865, 866, 868, 870, 871, 872, 873, 874, 875, 876, 877, 878, 879, 880, 881, 882, 883, 884, 885, 886, 887, 888, 889, 890, 891, 892, 893, 894, 895, 897, 898, 899, 900, 901, 902, 903, 904, 905, 906, 907, 908, 909, 910, 911, 912, 913, 914, 916, 917, 918, 919, 920, 921, 922, 923, 924, 925, 926, 929, 930, 934, 937, 938, 939, 944, 945, 947, 948, 953, 954, 955, 956, 957, 958, 960, 961, 962, 963, 964, 965, 968, 969, 971, 972, 1026, 1027, 1028, 1031, 1035, 1038, 1040, 1043, 1045, 1046, 1048, 1049, 1053, 1055, 1056, 1057, 1058, 1059, 1060, 1065, 1067, 1070, 1073, 1075, 1080, 1081, 1082, 1083, 1085, 1087, 1091, 1092, 1093, 1094, 1099, 1101, 1102, 1103, 1104, 1105, 1107, 1109, 1110, 1111, 1117, 1122, 1123, 1132, 1133, 1134, 1135, 1136, 1137, 1138, 1139, 1140, 1141, 1142, 1143, 1144, 1145, 1146, 1147, 1148, 1149, 1150, 1151, 1153, 1154, 1155, 1157, 1158, 1160, 1161, 1164, 1166, 1167, 1168, 1170, 1172, 1173, 1175, 1176, 1177, 1178, 1179, 1180, 1181, 1182, 1187, 1189, 1190, 1191, 1193, 1195, 1197, 1199, 1201, 1203, 1205, 1207, 1209, 1211, 1212, 1213, 1214, 1215, 1219, 1221, 1223, 1224, 1225, 1226, 1227, 1228, 1229, 1230, 1231, 1232, 1233, 1234, 1235, 1236, 1237, 1238, 1248, 1249, 1250, 1252, 1253, 1254, 1255, 1256, 1257, 1258, 1259, 1260, 1261, 1262, 1263, 1266, 1268, 1270, 1273, 1274, 1275, 1276, 1277, 1278, 1281, 1282, 1283, 1285, 1286, 1288, 1289, 1290, 1291, 1292, 1294, 1295, 1296, 1297, 1300, 1302, 1303, 1307, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324, 1325, 1326, 1327, 1328, 1330, 1331, 1332, 1333, 1334, 1335, 1336, 1338, 1339, 1340, 1341, 1342, 1343, 1344, 1347, 1350, 1351, 1352, 1353, 1354, 1356, 1357, 1358, 1362, 1366, 1367, 1368, 1369, 1370, 1371, 1373, 1375, 1377, 1379, 1380, 1381, 1382, 1383, 1389, 1390, 1391, 1392, 1393, 1394, 1395, 1396, 1397, 1398, 1399, 1400, 1401, 1402, 1403, 1404, 1405, 1410, 1411, 1412, 1413, 1414, 1415, 1416, 1417, 1418, 1419, 1420, 1422, 1423, 1424, 1427, 1428, 1429, 1433, 1434, 1437, 1438, 1439, 1440, 1441, 1442, 1443, 1444, 1445, 1446, 1448, 1449, 1451, 1452, 1454, 1465, 1471, 1472, 1476, 1477, 1480, 1481, 1482, 1483, 1484, 1485, 1486, 1488, 1489, 1491, 1492, 1493, 1494, 1495, 1496, 1497, 1498, 1499, 1501, 1502, 1503, 1504, 1505, 1506, 1507, 1508, 1509, 1511, 1512, 1513, 1515, 1518, 1519, 1520, 1521, 1523, 1524, 1525, 1527, 1528, 1532, 1533, 1534, 1540, 1547, 1550, 1551, 1552, 1553, 1554, 1555, 1556, 1557, 1558, 1559, 1563, 1565, 1567, 1569, 1572, 1573, 1574, 1575, 1576, 1577, 1578, 1579, 1580, 1581, 1582, 1583, 1584, 1585, 1586, 1587, 1588, 1589, 1590, 1591, 1592, 1593, 1594, 1595, 1596, 1597, 1598, 1599, 1600, 1601, 1602, 1603, 1604, 1605, 1606, 1607, 1608, 1609, 1610, 1611, 1613, 1614, 1615, 1616, 1617, 1618, 1619, 1620, 1621, 1622, 1623, 1624, 1625, 1626, 1627, 1628, 1629, 1630, 1631, 1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641, 1642, 1643, 1644, 1645, 1646, 1647, 1648, 1649, 1650, 1651, 1652, 1653, 1654, 1655, 1656, 1657, 1658, 1659, 1660, 1661, 1662, 1663, 1664, 1665, 1666, 1667, 1668, 1669, 1670, 1671, 1672, 1673, 1674, 1675, 1676, 1677, 1678, 1679, 1680, 1681, 1682, 1683, 1684, 1685, 1686, 1687, 1688, 1689, 1690, 1691, 1692, 1693, 1694, 1695, 1696, 1697, 1698, 1699, 1700, 1701, 1702, 1703, 1704, 1705, 1706, 1707, 1708, 1709, 1722, 1723, 1724, 1726, 1727, 1729, 1731, 1732, 1733, 1734, 1736, 1737, 1739, 1740, 1745, 1746, 1747, 1748, 1749, 1750, 1752, 1753, 1754, 1755, 1756, 1757, 1758, 1759, 1760, 1761, 1762, 1763, 1764, 1765, 1767, 1768, 1769, 1786, 1787, 1789, 1792, 1793, 1794, 1796, 1797, 1798, 1799, 1800, 1803, 1804, 1805, 1807, 1808, 1810, 1813, 1816, 1821, 1822, 1823, 1824, 1825, 1826, 1827, 1828, 1829, 1830, 1831, 1832, 1833, 1834, 1835, 1836, 1837, 1838, 1846, 1847, 1848, 1850, 1861, 1862, 1863, 1864, 1865, 1867, 1871, 1872, 1876, 1877, 1878, 1880, 1883, 1884, 1885, 1890, 1891, 1892, 1896, 1897, 1901, 1903, 1906, 1907, 1908, 1909, 1910, 1911, 1912, 1913, 1914, 1915, 1916, 1917, 1918, 1920, 1921, 1922, 1923, 1924, 1925, 1926, 1930, 1932, 1933, 1934, 1935, 1936, 1937, 1938, 1939, 1940, 1941, 1943, 1944, 1945, 1946, 1947, 1948, 1949, 1951, 1953, 1954, 1955, 1956, 1957, 1958, 1959, 1960, 1961, 1962, 1963, 1964, 1965, 1966, 1967, 1968, 1970, 1972, 1973, 1974, 1988, 1989, 1990, 1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2027, 2028, 2029, 2030, 2031, 2032, 2033, 2040, 2041, 2043, 2044, 2045, 2053, 2054, 2055, 2057, 2058, 2060, 2061, 2062, 2063};

    // A main method is defined as a method that is not called by any other methods, including recursively
    public static ArrayList<ArrayList<Integer>> AllMethodsCalled = new ArrayList<>();

    public static void GetAllMainMethods(String outputFile) {
        // Build out all method calls
        for (int i = 0; i < NUMBER_OF_METHODS; i++) {
            AssembledMethod a = ItemDefinition.getMethodByID(i);
            if (a == null) {
                continue;
            }
            ArrayList<Integer> ai = new ArrayList<>();
            for (int j = 0; j < a.assemblyInstructions.length; j++) {
                if (a.assemblyInstructions[j] == CS2AsmOpcodes.CALL.getOp() && !ai.contains((Object) a.instructionOperands[j])) {
                    ai.add(a.instructionOperands[j]);
                }
            }
            AllMethodsCalled.add(ai);
        }

        try {
            FileWriter myWriter = new FileWriter(outputFile);
            myWriter.write("Main methods:\n");
            String methos = "";
            for (int i = 0; i < NUMBER_OF_METHODS; i++) {
                boolean isMain = true;
                end:
                for (ArrayList<Integer> j : AllMethodsCalled) {
                    for (Integer k : j) {
                        if (i == k) {
                            isMain = false;
                            break end;
                        }
                    }
                }
                if (isMain) {
                    methos = methos + ", " + i;
                    myWriter.write("Method" + i + "\n");
                }
            }
            myWriter.write("\n\n" + methos);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Dumps the method arguments to a file for all methods, as well as a graph of their method calls and on what
     * lines those calls occur.
     */

    public static void DumpAllGameMethodArguments(String outputFile) {
        try {
            FileWriter myWriter = new FileWriter(outputFile);
            myWriter.write("; Game method metadata\n;\n");
            for (int l = 0; l < Integer.MAX_VALUE; l++) {
                AssembledMethod a = ItemDefinition.getMethodByID(l);
                if (a == null) {
                    myWriter.close();
                    break;
                } else {
                    myWriter.write(";Method" + l + " (");
                    int i = 0;
                    for (int j = 0; j < a.numberOfIntArguments; j++) {
                        i++;
                        myWriter.write("int var" + i);
                        if (j < a.numberOfIntArguments - 1 || a.numberOfStringArguments != 0) {
                            myWriter.write(", ");
                        }
                    }
                    for (int j = 0; j < a.numberOfStringArguments; j++) {
                        i++;
                        myWriter.write("string var" + i);
                        if (j < a.numberOfStringArguments - 1) {
                            myWriter.write(", ");
                        }
                    }
                    myWriter.write(")\n");
                    GetMethodDependencyGraph(myWriter, l);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred writing the file: " + outputFile);
            e.printStackTrace();
        }
    }

    /**
     * Records for each method, all the methods that it calls, recursively. Used internally by other functions.
     */
    private static int treeDepth = 0;
    private static ArrayList<Integer> tree = new ArrayList<>();

    private static void GetMethodDependencyGraph(FileWriter f, int methodID) throws IOException {
        AssembledMethod am = ItemDefinition.getMethodByID(methodID);
        for (int i = 0; i < treeDepth; i++) {
            f.write('\t');
        }
        f.write("Method" + methodID + "\n");
        if (am == null) {
            f.write(" Null??");
            return;
        }
        treeDepth++;
        tree.add(methodID);
        for (int i = 0; i < am.assemblyInstructions.length; i++) {
            if (am.assemblyInstructions[i] == CS2AsmOpcodes.CALL.getOp()) {
                f.write(i + ":");
                if (tree.contains(am.instructionOperands[i])) {
                    treeDepth++;
                    for (int j = 0; j < treeDepth; j++) {
                        f.write('\t');
                    }
                    f.write("RECURSIVE Method" + methodID);
                    treeDepth--;
                } else {
                    GetMethodDependencyGraph(f, am.instructionOperands[i]);
                }
            }
        }
        tree.remove(tree.size() - 1);
        treeDepth--;
    }

    public static void PrintAllValidGameMethods() {
        for (int l = -5000; l < 5000; l++) {
            AssembledMethod a = ItemDefinition.getMethodByID(l);
            if (a == null) {
                continue;
            }
            System.out.println("Valid methodID: " + l);
        }
    }
}
