(ns advent-of-code-2025.day8.day8 
  (:require
    [clojure.string :as str]
    [clojure.set :as set]))

;; Add a sqrd method
(defn distance [[x1 y1 z1] [x2 y2 z2]]
  (Math/sqrt
   (+ (Math/pow (- x1 x2) 2) (Math/pow (- y1 y2) 2) (Math/pow (- z1 z2) 2))))

(distance [1 2 4] [3 -23 41])

;; My naive solution will be n^2. I'm going to find the distance between each node first

(def test-input "./src/advent_of_code_2025/day8/test_input.txt")

(defn parse-coords [input]
  (->> (slurp input)
       (str/split-lines)
       (map #(str/split % #","))
       (map #(map parse-long %))))

(def test-coords
  (parse-coords test-input))

(defn coord-key [c1 c2]
  (str (vec c1) "," (vec c2)))

(coord-key (first test-coords) (second test-coords))

;; gen routes or something. Anyway. I'm tired right now
(defn gen-distance-map [coords]
  (apply merge
         (for [c1 coords
               c2 coords
               :when (not= c1 c2)]
           {(coord-key c1 c2) (distance c1 c2)
            (coord-key c2 c1) (distance c1 c2)})))

(->
 (gen-distance-map test-coords)
 (get (coord-key (first test-coords) (second test-coords)))
 )

(sort-by val < 
         (gen-distance-map (parse-coords test-input)))

;; It looks like I'll iterate through the distance map, and connect, and see what graph is created

;; Can use reduce to build the graph. To calc the segments, that's probably a graph traversal
;; with some duplicated set. Maybe I'll need to memoize like the last problem

(defn parse-nodes [route]
  (->> (str/split route #",")
       (map #(re-seq #"\d+" %)) 
       (map #(map parse-long %))))

(parse-nodes "[592 479 940],[862 61 35]")

(defn build-graph [[n1 n2]]
  (hash-map (str (random-uuid))
            {:connections {n1 #{n2} , n2 #{n1}}})) ; I may not need the connections keyword, could flatten this

(def test-graph
  (build-graph ['(1 2 3) '(4 5 6)]))

(vals test-graph)

;; fix this
(defn has-node? [graph n]
  (->> (vals graph)
       first
       :connections
       keys
       (some #(= n %))
       boolean
       ))

(has-node? test-graph [1 2 3])

;; all graphs, in this case is not a collection, but a map, in this case
(defn find-corresponding-graph [all-graphs n]
    (->> all-graphs
         
       (filter #(has-node? % n))        ;      first
       first))

; I'm going to use a record class. The models are getting too confusing

(find-corresponding-graph test-graph '(1 2 3))

(defn find-graphs-for-nodes [all-graphs [n1 n2]]
    (let [n1-graph (find-corresponding-graph all-graphs n1)
        n2-graph (find-corresponding-graph all-graphs n2)]
    {n1 n1-graph, n2 n2-graph}))

(defn update-connections [connections [n1 n2]]
  (merge connections {n1 (conj (get connections n1 []) n2)
                      n2 (conj (get connections n2 []) n1)}))

; kind of works, make sure everything is clojure list
(update-connections (:connections (first (vals test-graph))) [[3 4 5] [4 5 6]])

(defn add-to-graph [all-graphs graphs-for-nodes]
  (let [[n1 n2] (keys graphs-for-nodes)]
    (if (nil? (get graphs-for-nodes n1))
      (update-in all-graphs [n2 :connections] #(update-connections % [n1 n2]))
      (update-in all-graphs [n1 :connections] #(update-connections % [n1 n2])))))

(defn merge-graphs [all-graphs graphs-for-nodes]
  ) ;; not doing anything yet

(defn- calc-num-existing-connections [graphs-for-nodes]
  (count (filter seq (vals graphs-for-nodes))))

(defn update-graphs [all-graphs graphs-for-nodes]
  (condp = (calc-num-existing-connections graphs-for-nodes)
    0 (conj all-graphs (build-graph (keys graphs-for-nodes)))
    1 (add-to-graph all-graphs graphs-for-nodes)
    2 (merge-graphs all-graphs graphs-for-nodes)))

(defn build-graphs [all-graphs [route _]]
  (let [nodes (parse-nodes route)
        graphs-for-nodes (find-graphs-for-nodes all-graphs nodes)]
    (update-graphs all-graphs graphs-for-nodes)))

(defn connect-circuits [distance-map]
  (let [sorted-distance-map (sort-by val < distance-map)]
    (reduce build-graphs {} sorted-distance-map)))

(connect-circuits (gen-distance-map test-coords))
