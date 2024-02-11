(ns advent-of-code.day3.day3)

(defn is-digit?
  "Check if the given character represents a digit."
  [char]
  (try
    (Integer/parseInt (str char))
    true
    (catch NumberFormatException _
      false)))

(defn is-symbol?
  "docstring"
  [char]
  (not (or (is-digit? char) (= "." char))))

(defn is-in-graph?
  "docstring"
  [[x y] graph]
  (let [graph-length (count (first graph))
        graph-height (count graph)]
    (and (> graph-length x) (> graph-height y) (not (neg-int? x)) (not (neg-int? y)))))


(defn get-graph-value
  "docstring"
  [graph [x y]]
  (nth (nth graph y) x))

(defn sum-of-parts
  "docstring"
  [graph]
  (let [row (first graph)]
    (loop [index 0
           remaining row
           current-num ""
           current-num-index nil]
      (cond
        (empty? remaining)
        (if (empty? current-num)
          0
          (let [left-neighbor [(dec current-num-index) 0]]  ; add more tests, change to all-neighbors or something. tdd can be weird
            (if (and (is-in-graph? left-neighbor graph) (is-symbol? (get-graph-value graph left-neighbor)))
              (Integer/parseInt current-num)
              0)))
        (is-digit? (first remaining))
        (recur (inc index) (rest remaining) (str current-num (first remaining)) (if (nil? current-num-index) index current-num-index))
        :else
        (recur (inc index) (rest remaining) current-num current-num-index)))))