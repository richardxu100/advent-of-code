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

(defn count-map [hand]
  (loop [remaining hand
         result {}]
    (if (empty? remaining)
      result
      (if (contains? result (first remaining))
        (recur (rest remaining) (assoc result (first remaining) (inc (get result (first remaining)))))
        (recur (rest remaining) (assoc result (first remaining) 1))))))

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
