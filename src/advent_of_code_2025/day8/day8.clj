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

(defn build-graph [n1 n2]
  {:nodes #{n1 n2} :connections {n1 #{n2} , n2 #{n1}}})

(build-graph '(1 2 3) '(4 5 6))

(defn find-containing-graph-id [graphs n]
   (first (filter #(contains? (:nodes %) n) graphs)))

(defn find-containing-graph-ids [graphs [n1 n2]]
   (let [n1-graph-id (find-containing-graph-id graphs n1)
         n2-graph-id (find-containing-graph-id graphs n2)]
     (filter seq [n1-graph-id n2-graph-id])))

(defn update-connections [connections [n1 n2]]
  (merge connections {n1 (conj (get connections n1 []) n2)
                      n2 (conj (get connections n1 []) n1)}))

;; nodes is probably redundant, as that can be derived from the keys of the connections
(defn add-to-graph [graphs graph-id [n1 n2]]
  (let [associated-graph (get graphs graph-id)
        updated-graph {:nodes (conj (:nodes associated-graph) n1 n2)
                       :connections (update-connections (:connections associated-graph) [n1 n2])}]
    (update graphs graph-id updated-graph)))

(defn merge-graphs [graphs graph-ids [n1 n2]]
  graphs) ;; not doing anything yet

(defn update-graphs [graphs graph-ids [n1 n2]]
  (condp = (count graph-ids)
    0 (conj graphs (build-graph n1 n2))
    1 (add-to-graph graphs (first graph-ids) [n1 n2])
    2 (merge-graphs graphs graph-ids [n1 n2])))

;; I'm going to try to use a clojure list as a map key
(defn build-graphs [graphs [route _]]
  (let [[n1 n2] (parse-nodes route)
        graph-ids (find-containing-graph-ids graphs [n1 n2])]
    (update-graphs graphs graph-ids [n1 n2])))

(defn connect-circuits [distance-map]
  (let [sorted-distance-map (sort-by val < distance-map)]
    (reduce build-graphs {} sorted-distance-map)))

(connect-circuits (gen-distance-map test-coords))


