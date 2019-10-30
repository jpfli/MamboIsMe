//!APP-HOOK: pre-build

// This script looks for level and world map XML files in the "maps" folder
// and converts them to binary data.

// Maps object symbols to numeric code
let obj_symbols = {
    '.':0, // Empty tile
    '=':1, // Verb: Is
    
    // Properties: Me, Goal, End, Stop, Push, Death, Raze, Walk,
    // Hot, Melt, Hover
    '@':8, '$':9, '|':10, '!':11, '>':12, '+':13, '_':14, ':':15, 
    '*':16, '~':17, '^':18, 
    // Nouns: Mambo, Doll, Vampyr, Snake, Zombie, Garlic, Fire, Brain
    // Skull, Mojo, Wall, Bog
    'M':32, 'D':33, 'V':34, 'S':35, 'Z':36, 'G':37, 'F':38, 'C':39, 
    'N':40, 'K':41, 'J':42, 'W':43, 'B':44, 
    // Characters: Mambo, Doll, Vampyr, Snake, Zombie, Garlic, Fire, Brain
    // Skull, Mojo, Wall, Bog
    'm':48, 'd':49, 'v':50, 's':51, 'z':52, 'g':53, 'f':54, 'c':55, 
    'n':56, 'k':57, 'j':58, 'w':59, 'b':60
};

// Maps direction symbols to numeric code
let dir_symbols = {
    // default, right, up, left, down
    '.':0, 'r':0, 'u':1, 'l':2, 'd':3
};

let filePaths = (dir("maps") || []).filter(file => /\.xml$/i.test(file)).map(file => `maps/${file}`);

for(let filePath of filePaths) {
    xml = XML(read(filePath));
    let basename = path.basename(filePath);
    // Remove last '.' plus everything after it, then replace last '_' with '.'
    let filename = basename.substr(0, basename.lastIndexOf(".")).replace(/_([^_]*)$/,"."+'$1');
    
    if(xml.querySelector("map")) {
        let map = {};
        for(let attr of [...xml.querySelector("map").attributes]) {
            map[attr.name] = /^[0-9]+$/.test(attr.value) ? attr.value|0 : attr.value;
        }
        map.hint = getHintText(xml);
        map.layers = getLayers(xml, map.width, map.height);
        
        writeMap(filename, map);
    }
    else if(xml.querySelector("bytestring")) {
        // text = "".join(text[text.find("\"")+1:].rstrip().rstrip("\"").split("\"\\\n\""))
        let text = xml.querySelector("bytestring").textContent.trim();
        text = text.replace(/[^\S\n]/g, "").replace(/(\\\n|\")/g, "");
        
        // data = [int(x, 16) for x in text.lstrip("\\x").split("\\x")]
        data = text.match(/[^(\\x)]+/g).map(x => parseInt(x, 16));

        writeByteString(filename, data);
    }
}



// let maps = (dir("maps") || [])
//     .filter( file => /\.xml$/i.test(file) )
//     .map( file => readMap(`maps/${file}`) );

// for( let map of maps ){
//     if(!map) {
//         continue;
//     }
//     writeMap(map);
// }

// Tell the build system we're done
if(hookTrigger == "pre-build")
    hookArgs[1]();

function readMap(filePath) {
    let xml = XML(read(filePath));
    
    if(xml.querySelector("map")) {
        let map = {};
        let basename = path.basename(filePath);
        map.filename = basename.substr(0, basename.lastIndexOf(".")); // .replace(/\..*/, "");
        for(let attr of [...xml.querySelector("map").attributes]) {
            map[attr.name] = /^[0-9]+$/.test(attr.value) ? attr.value|0 : attr.value;
        }
        map.hint = getHintText(xml);
        map.layers = getLayers(xml, map.width, map.height);
        
        return map;
    }
    return null;
}

function getHintText(xml) {
    let hint = xml.querySelector("hint").textContent.trim();
    
    // Ensure that the hint text is single line
    return hint.split("\n").map(row=>row.trim()).join(" ");
}

function getLayers(xml, width, height){
    let pad = Array(width+1).join(".");
    let layers = [...xml.querySelectorAll("layer")].map(layer=>{
        // let data = layer.textContent.trim().split("").map(x=>x.charCodeAt(0));
        
        // Extract text content as lines, height lines max
        let rows = layer.textContent.trim().split("\n").slice(0,height);
        
        // Add empty rows if number of rows is less than height
        while(rows.length<height) {
            rows.push(pad);
        }
        
        // Make rows width characters long
        rows = rows.map(row=>(row.trim()+pad).substr(0,width));
        
        // Merge rows into one string
        let data = rows.join("").split("").map(x=>x.charCodeAt(0));
        
        [...layer.attributes].reduce((obj, attr)=>(obj[attr.name] = attr.value, obj), data);
        Object.assign(data, getProperties(layer));
        
        return data;
    }); // .reduce((map, obj)=>(map[obj.name] = obj, map), {});
    
    // return layers;
    return layers.reduce((map, obj)=>(map[obj.id] = obj, map), {});
}

function writeMap(filename, map) {
    let out = [];
    
    let obj_layer = map.layers.objects;
    let dir_layer = map.layers.facing;
    
    let end = map.width*map.height;
    for(let idx=0; idx<end; ++idx) {
        let key = String.fromCharCode(obj_layer[idx]);
        let obj_code = (key in obj_symbols) ? obj_symbols[key] : 0;
        
        key = String.fromCharCode(dir_layer[idx]);
        let dir_code = (key in dir_symbols) ? dir_symbols[key] : 0;
        
        // let code = obj_code | (dir_code<<6);
        // out = out.concat(("["+code+"], ").split("").map(c=>c.charCodeAt(0)));
        
        out.push(obj_code | (dir_code<<6));
    }
    
    // After map data comes level name and the hint text as zero terminated strings
    out = out.concat((map.name+"\0"+map.hint+"\0").split("").map(c => c.charCodeAt(0)));
    
    write(`maps/${filename}`, new Uint8Array(out), undefined);
    // write(`maps/${map.name}Data.map`, new Uint8Array(map), undefined);
}


function writeByteString(filename, data) {
    // let out = bytestring.split("").map(c => c.charCodeAt(0));

    write(`maps/${filename}`, new Uint8Array(data), undefined);
}

function getProperties(node){
    return [...node.querySelectorAll("property")].reduce((o, prop)=>{
                        o[prop.attributes.name.value.trim()] = prop.attributes.value.value.trim();
                        return o;
                    }, {});
}
