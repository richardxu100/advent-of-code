(ns advent-of-code.day3.day3)

(defn find-neighbor-indices
  "docstring"
  [x y]
  [[(- x 1) (- y 1)
    (- x 1) y
    (- x 1) (+ y 1)
    x (- y 1)
    x (+ y 1)
    (+ x 1) (- y 1)
    (+ x 1) y
    (+ x 1) (+ y 1)
    ]])

(defn is-in-graph?
  "docstring"
  [[x y] graph]
  (let [graph-length (count (first graph))
        graph-height (count graph)]
    (and (> graph-length x) (> graph-height y)))
   )

(defn is-digit?
  "docstring"
  [char]
  (contains? #{"1" "2" "3" "4" "5" "6" "7" "8" "9" "0"} char) )

(defn is-symbol?
  "docstring"
  [char]
  (not (or (is-digit? char) (= "." char))))

(defn find-nums
  "docstring"
  [row]
  (loop [remaining row
         current-number ""
         nums []]
    (cond
      (and (empty? remaining) (seq current-number))
      (conj nums (Integer/parseInt current-number))
      (empty? remaining)
      nums
      (and (not (is-digit? (first remaining))) (not (empty? current-number)))
      (recur (rest remaining) "" (conj nums (Integer/parseInt current-number)))
      (is-digit? (first remaining))
      (recur (rest remaining) (str current-number (first remaining)) nums)
      :else
      (recur (rest remaining) current-number nums))))

(find-nums ["4" "6" "7" "." "1" "2"])

(defn split-string-into-list [s]
  (map str (seq s)))

(split-string-into-list "467..114..")

(defn sum-of-parts
  "docstring"
  [graph]
  )


