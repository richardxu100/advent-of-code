(ns advent-of-code-2025.day8.day8 
  (:require
    [clojure.string :as str]
    [clojure.set :as set]))

(defn sqrd [x]
  (Math/pow x 2))

;; Add a sqrd method
(defn distance [[x1 y1 z1] [x2 y2 z2]]
  (Math/sqrt
   (+ (sqrd (- x1 x2)) (sqrd (- y1 y2)) (sqrd (- z1 z2)))))

(distance [1 2 4] [3 -23 41])

;; My naive solution will be n^2. I'm going to find the distance between each node first

(def test-input "./src/advent_of_code_2025/day8/test_input.txt")
(def input "./src/advent_of_code_2025/day8/input.txt")

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

; I can simplify this. I'm just going to add one of the routes, not the reverse, which is the same
(defn gen-distance-map [coords]
  (apply merge
         (for [c1 coords
               c2 coords
               :when (not= c1 c2)]
           {(coord-key c1 c2) (distance c1 c2)})))

(defn gen-sorted-distance-map [coords]
  (->> (gen-distance-map coords)
       (sort-by val <)
       (take-nth 2))) ; last step removes the duplicate routes

(comment
  (gen-sorted-distance-map test-coords)
  (->
   (gen-distance-map test-coords)
   (get (coord-key (first test-coords) (second test-coords))))
  (take-nth 2 (sort-by val <
                       (gen-distance-map (parse-coords test-input)))))

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

; val works on a map entry. vals works on the entire map
(defn has-node? [circuit-entry n]
  (->> (val circuit-entry)
       :connections
       keys
       (some #(= n %))
       boolean))

;; all graphs, in this case is not a collection, but a map, in this case
(defn find-corresponding-circuit-id [circuits n]
  (->> circuits
       (filter #(has-node? % n))        ;      first
       first
       first))

; A map entry looks like a two element array of the key and value
; That makes sense
(find-corresponding-circuit-id test-circuit '(1 2 3))

(defn find-circuit-ids-for-nodes [circuits [n1 n2]]
  (let [n1-circuit-id (find-corresponding-circuit-id circuits n1)
        n2-circuit-id (find-corresponding-circuit-id circuits n2)]
    {n1 n1-circuit-id, n2 n2-circuit-id}))

(defn update-connections [connections [n1 n2]]
  (merge connections {n1 (conj (get connections n1 #{}) n2)
                      n2 (conj (get connections n2 #{}) n1)}))

(comment
  (update-connections (:connections (first (vals test-circuit))) [[3 4 5] [4 5 6]]))

(defn add-to-circuit [circuits circuit-ids-for-nodes]
  (let [[n1 n2] (keys circuit-ids-for-nodes)
        n1-circuit-id (get circuit-ids-for-nodes n1)
        n2-circuit-id (get circuit-ids-for-nodes n2)]
    (if (nil? n1-circuit-id)
      (update-in circuits [n2-circuit-id :connections] #(update-connections % [n1 n2]))
      (update-in circuits [n1-circuit-id :connections] #(update-connections % [n1 n2])))))

(defn- build-merged-circuit [circuits circuit-ids-for-nodes]
  (let [[n1 n2] (keys circuit-ids-for-nodes)
        [circuit-id1 circuit-id2] (vals circuit-ids-for-nodes)
        circuit1 (get circuits circuit-id1)
        circuit2 (get circuits circuit-id2)
        merged-connections (merge (:connections circuit1) (:connections circuit2))
        updated-connections (merge merged-connections {n1 (conj (get merged-connections n1) n2)
                                                       n2 (conj (get merged-connections n2) n1)})]
    (hash-map (str (random-uuid))
              {:connections updated-connections})))

(comment
  (def test-circuit-1 (build-circuit ['(1 2 3) '(4 5 6)]))
  (def test-circuit-2 (build-circuit ['(:a :b :c) '(:d :e :f)]))

  (def test-circuit-1-id (first (keys test-circuit-1)))
  (def test-circuit-2-id (first (keys test-circuit-2)))

  (build-merged-circuit (merge test-circuit-1 test-circuit-2) {'(4 5 6) test-circuit-1-id
                                                               '(:d :e :f) test-circuit-2-id}))

; not always needed, if in the same circuit... Then can just do nothing
; Looks like it doesn't hurt, I should probably remove the connection though here. May hurt for part 2
(defn merge-circuits [circuits circuit-ids-for-nodes]
  (let [merged-circuit (build-merged-circuit circuits circuit-ids-for-nodes)
        [circuit-id1 circuit-id2] (vals circuit-ids-for-nodes)]
    (-> circuits
        (dissoc circuit-id1 circuit-id2)
        (merge merged-circuit))))

(merge-circuits (merge test-circuit-1 test-circuit-2) {'(4 5 6) test-circuit-1-id
                                                             '(:d :e :f) test-circuit-2-id})


(defn- calc-num-existing-circuits [circuit-ids-for-nodes]
  (->> circuit-ids-for-nodes
       vals
       (filter seq)
       count))

(defn- in-same-circuit? [circuit-ids-for-nodes]
  (= 1 (count (set (vals circuit-ids-for-nodes)))))

(defn update-circuits [circuits circuit-ids-for-nodes]
  (condp = (calc-num-existing-circuits circuit-ids-for-nodes)
    0 (conj circuits (build-circuit (keys circuit-ids-for-nodes)))
    1 (add-to-circuit circuits circuit-ids-for-nodes)
    2 (if (in-same-circuit? circuit-ids-for-nodes)
        circuits
        (merge-circuits circuits circuit-ids-for-nodes))))

(defn build-circuits [circuits [route _]]
  (let [nodes (parse-nodes route)
        circuit-ids-for-nodes (find-circuit-ids-for-nodes circuits nodes)]
    (update-circuits circuits circuit-ids-for-nodes)))

(defn connect-circuits [sorted-distance-map num-to-connect]
  (reduce build-circuits {} (take num-to-connect sorted-distance-map)))

(comment
  (in-same-circuit? {'(1 2 3) "abc" '(4 5 6) "abcd"})
  (connect-circuits (gen-sorted-distance-map test-coords) 4)
  (def test-sorted-distance-map (gen-sorted-distance-map test-coords))
  test-sorted-distance-map)

(defn num-nodes-in-circuit [[_ {connections :connections}]]
  (count (keys connections)))


(defn part1 [input num-connections]
  (let [coords (parse-coords input)
        sorted-distance-map (gen-sorted-distance-map coords)
        circuits (connect-circuits sorted-distance-map num-connections)
        three-largest-circuits (take 3 (sort-by num-nodes-in-circuit > circuits))]
    (reduce * (map num-nodes-in-circuit three-largest-circuits))))

(part1 test-input 10)
(part1 input 1000)



