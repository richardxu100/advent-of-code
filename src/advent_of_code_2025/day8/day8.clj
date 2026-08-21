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

(defn build-circuit [[n1 n2]]
  (hash-map (str (random-uuid))
            {:connections {n1 #{n2} , n2 #{n1}}})) ; I may not need the connections keyword, could flatten this

(def test-circuit
  (build-circuit ['(1 2 3) '(4 5 6)]))

; (vals test-circuit)

;; fix this
(defn has-node? [circuit-entry n]
  (->> (val circuit-entry)
       :connections
       keys
       (some #(= n %))
       boolean))

;; all graphs, in this case is not a collection, but a map, in this case
(defn find-corresponding-circuit-id [circuits n]
  #dbg
  (->> circuits
       (filter #(has-node? % n))        ;      first
       first
       first
       ))


; A map entry looks like a two element array of the key and value
; That makes sense
(find-corresponding-circuit-id test-circuit '(1 2 3))

(defn find-circuit-ids-for-nodes [circuits [n1 n2]]
  (let [n1-circuit-id (find-corresponding-circuit-id circuits n1)
        n2-circuit-id (find-corresponding-circuit-id circuits n2)]
    {n1 n1-circuit-id, n2 n2-circuit-id}))

(defn update-connections [connections [n1 n2]]
  (merge connections {n1 (conj (get connections n1 []) n2)
                      n2 (conj (get connections n2 []) n1)}))

; kind of works, make sure everything is clojure list
(update-connections (:connections (first (vals test-circuit))) [[3 4 5] [4 5 6]])

(defn add-to-circuit [circuits circuit-ids-for-nodes]
  (let [[n1 n2] (keys circuit-ids-for-nodes)
        n1-circuit-id (get circuit-ids-for-nodes n1)
        n2-circuit-id (get circuit-ids-for-nodes n2)]
    (if (nil? n1-circuit-id)
      (update-in circuits [n2-circuit-id :connections] #(update-connections % [n1 n2]))
      (update-in circuits [n1-circuit-id :connections] #(update-connections % [n1 n2])))))

(defn merge-circuits [all-circuits graphs-for-nodes]
  all-circuits
  ) ;; not doing anything yet

(defn- calc-num-existing-circuits [circuit-ids-for-nodes]
  (count (filter seq (vals circuit-ids-for-nodes))))

(defn update-graphs [circuits circuit-ids-for-nodes]
  (condp = (calc-num-existing-circuits circuit-ids-for-nodes)
    0 (conj circuits (build-circuit (keys circuit-ids-for-nodes)))
    1 (add-to-circuit circuits circuit-ids-for-nodes)
    2 (merge-circuits circuits circuit-ids-for-nodes)))

(defn build-circuits [circuits [route _]]
  (let [nodes (parse-nodes route)
        circuit-ids-for-nodes (find-circuit-ids-for-nodes circuits nodes)]
    (update-graphs circuits circuit-ids-for-nodes)))

(defn connect-circuits [distance-map]
  (let [sorted-distance-map (sort-by val < distance-map)]
    (reduce build-circuits {} sorted-distance-map)))

(connect-circuits (gen-distance-map test-coords))
