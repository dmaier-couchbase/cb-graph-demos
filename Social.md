# Simpsons as a Social Network

The Simpsons universe has a lot of fictitious characters. So the idea is to extract them from Wikisimpsons.com and create a social Graph of them.

## Categories

SimpsonWiki is a MediaWiki which means that it supports categories. We are currently interested in the following ones:

* Characters
* Families

## Export format

Each entry has the following format:

```
<page>
  <title>$Title</title>
  <id>$Id</id>
  <text>$Text</text>
</page>
```

A text might contain a two column table like the following one:

```
Each Text has a table with the optional attributes, e.g.:
{{Animal
|name = Pikkanoze
|image = [[File:Pikkanoze.png|250px]]
|species = Porkymen
|gender = {{Male}}
|color = Yellow and black
|age = E
|status = Non-canon
|owner = 
|relatives = 
|job =  
|appearance = ''[[Mr. Sparkle: Destroy All Manga!]]''
|voiced by = 
}}
```

A text indeed also contains links to other wiki articles.

## Connections

Some connections are obvious:

* relatives (see table above)
  * This is most often pointing to a family
* relatedTo (any WikiLink which is resolvable)

Other connections need basic text understanding and rules in order to derive them:

* Friend of
  * A character is a friend of another character if there is an occourence of 'friend of [[OtherCharacter]]' in the text.
  * The friend of relationship is bidirectional 
* Enemy of
  * A chracter is an enemy of another character if there is an occourence of 'enemy of [[OtherCharacter]]' in the text.
  * The enemy relationship is uni-directional because another person might not know of his enemy
* Family relationships from the text
  * father of [[AnotherCharacter]]
  * mother of [[AnotherCharacter]]
  * daughter of [[AnotherCharacter]]
  * son of [[AnotherCharacter]]
  * child of [[AnotherCharacter]]
  * wife of [[AnotherCharacter]]
  * husband of [[AnotherCharacter]]
  * brother of [[AnotherCharacter]]
  * sister of [[AnotherCharacter]]
  
## Implementation idea

* We will write a simple import tool (Java) 
* It will parse the XML (by using a ready to use parsing library)
* The id of the vertex will be the actual Wiki link Id, e.g. [[Maggie Simpson]] translates to [[Maggie_Simpson]]
  * Have to find out how this translation is done from the title
* In the first step we create all the vertices as skeletons in CBGraph
  * id
  * title
* In the second step we parse the two column tables in order to derive attributes
  * First column value is the property name
  * Second column value is the property value
  * A property is only set if it has a value
* In the third step we add the relationships
  * The links can have several formats and need to be normalized
  
