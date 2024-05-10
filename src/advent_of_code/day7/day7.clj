(ns advent-of-code.day7.day7)

(def ranking-map
  {"2" 2
   "3" 3
   "4" 4
   "5" 5
   "6" 6
   "7" 7
   "8" 8
   "9" 9
   "T" 10
   "J" 11
   "Q" 12
   "K" 13
   "A" 14})

(ranking-map "A")

(defn has-pair? [hand]
  (= (count hand) (inc (count (set hand)))))


(defn has-two-pair? [hand]
  (= (count hand) (+ 2 (count (set hand)))))

(defn my-count-map [hand]
  (loop [remaining hand
         result {}]
    (if (empty? remaining)
      result
      (if (contains? result (first remaining))
        (recur (rest remaining) (assoc result (first remaining) (inc (get result (first remaining)))))
        (recur (rest remaining) (assoc result (first remaining) 1))))))

(defn count-map
  "docstring"
  [hand]
  (reduce (fn [result item] (update result item (fnil inc 0))) {} hand)) ; fnil will first try to use inc, but if it's passed in nil, it will give a 0

(defn has-three-of-a-kind? [hand]
  (some #(= 3 %) (vals (count-map hand))))

(defn calc-hand-type
  "docstring"
  [hand]
  (cond
    (has-three-of-a-kind? hand)
    :three-of-a-kind
    (has-two-pair? hand)
    :two-pair
    (has-pair? hand)
    :one-pair
    :else
    :high-card)
  )

(count-map "111234")
(new-count-map "111234")