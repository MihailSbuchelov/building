var MODEL_FLOOR_FIELD_NAME = "Уровень";
var MODEL_BLOCK_FIELD_NAME = "Номер секции";
var MODEL_ARTICLE_TYPE_FIELD_NAME = "Type Группа изделия";

var status_materials = {};

var scene, camera, renderer, loader, controls;
var width = 800;
var height = 600;

var levelMap = { };
var blockMap = { };
var articleTypeMap = { };
var itemsMap = { };
var planItems = undefined;

function init(domId){
  THREE.log("init", domId);
  // init loader
  loader = new THREE.ObjectLoader();
  //Init renderer
  if (Detector.webgl){
    THREE.log("Use WebGLRenderer");
    renderer = new THREE.WebGLRenderer( {antialias:true} );
  } else{
    THREE.log("Use CanvasRenderer");
    renderer = new THREE.CanvasRenderer(); 
  }
  renderer.setSize(width, height);
  renderer.setClearColor( 0xdddddd, 1);
  var container = document.getElementById(domId);
  container.appendChild(renderer.domElement);
}

function extractArticleFromModelName(name){
	if(!name) return;
	var regexp = /^.*<\d+ (.+)>$/i;
	if(!regexp.test(name))
		return;
	var found = name.match(regexp);
	return found[1].toLowerCase();
};

function extractBlock(blockStr){
	if(!blockStr) return;
	return blockStr.toLowerCase().trim();
}

function extractFloorNumber(floorStr){
  if(!floorStr) return;
  var regexp = /\D*([0-9]+)\D*$/i;
  if(!regexp.test(floorStr))
	  return "Чердак";
  
  var found = floorStr.match(regexp);
  return found[1];
};

function extractArticleType(articleTypeStr){
	if(!articleTypeStr) return;
	return articleTypeStr.toLowerCase().trim();
}

function setDefaultStatus(status){
	// All children without status to new status
	for(var i = 0; i < scene.children.length; i++){
		var obj = scene.children[i];
		if(!!obj.status)
			continue;
		obj.status = "new";
		if(!!obj.children[0]){
			obj.children[0].material = status;
		}
	}
}

var pointLight, center;

// Функция отрабатывает после загрузки сцены
function sceneLoadedCallback(loadedScene){
	scene = loadedScene;
	
	THREE.log("Scene loaded", loadedScene);
	// Настраиваем материалы по статусам изделий
	status_materials.NEW = new THREE.MeshPhongMaterial({
		transparent: true, 
		opacity : 0.1,
		color: 0x7f7f7f, 
		shininess: 1
	});
	status_materials.PLAN = new THREE.MeshPhongMaterial({
		transparent : true,
		opacity	 : 0.6,
		color : 0xe0d0a0,
		shininess : 1
	});
	status_materials.STOCK = new THREE.MeshPhongMaterial({
		transparent : true,
		opacity	 : 0.6,
		color : 0xa0e0a0,
		shininess : 1
	});
	status_materials.DONE = new THREE.MeshPhongMaterial({
		color : 0x7f7f7f,
		shininess : 1
	});
	
	// Вычисляем середину сцены
	var boundBox = new THREE.Box3();
	boundBox.setFromObject(loadedScene);
	center = boundBox.center();

	// Заполняем коллекции для фильтрации объектов сцены
	for(var i = 0; i < scene.children.length; i++){
		var obj = scene.children[i];
//		obj.castShadow = true;
//		obj.receiveShadow = true;
//		if(obj.children[0]){
//			obj.children[0].castShadow = true;
//			obj.children[0].receiveShadow = true;
//		}
		var userData = obj.userData;
		
		var name = extractArticleFromModelName(obj.name);
		var levelStr = userData[MODEL_FLOOR_FIELD_NAME];
		var level = extractFloorNumber(levelStr);
		var blockStr = userData[MODEL_BLOCK_FIELD_NAME];
		var block = extractBlock(blockStr);
		var articleTypeStr = userData[MODEL_ARTICLE_TYPE_FIELD_NAME];
		var articleType = extractArticleType(articleTypeStr);
		
		if(!!level){
			if(!levelMap[level]){
				levelMap[level] = [];
			}
			levelMap[level].push(obj);
		}
		if(!!block){	
			if(!blockMap[block]){
				blockMap[block] = [];
			}
			blockMap[block].push(obj);
		}
		
		if(!!articleType){
			if(!articleTypeMap[articleType]){
				articleTypeMap[articleType] = [];
			}
			articleTypeMap[articleType].push(obj);
		}
		
		if(!!name && !!block && !!level){
			var key = name + ":" + level + ":" + block;
			if(!itemsMap[key]){
				itemsMap[key] = [];
			}
			itemsMap[key].push(obj);
		}
	}
	
	// Источник света от камеры
	pointLight = new THREE.PointLight( 0xffffff, 1.25 );
    scene.add( pointLight );
	
	// Создание камеры
	THREE.log("Create camera");
	camera = new THREE.PerspectiveCamera(50, width/height, 1, 1000000);
	
	scene.add(camera)
	camera.position.x = boundBox.min.x * 1.6;
	camera.position.y = boundBox.max.y * 1.6;
	camera.position.z = boundBox.max.z * 1.6; 
	camera.lookAt(center);
	
	// Управление камерой
	controls = new THREE.OrbitControls( camera );
	controls.damping = 0.2;
	controls.addEventListener( 'change', render );
	controls.target = center;
	
	if(!!planItems){// Если загружена информация по изделиям отмечаем цветом в зависимости от статуса
		loadPlanItems(planItems);
	} else{ // Иначе отмечаем все изделия как построенные
		setDefaultStatus(status_materials.DONE);
	}
	
	animate();
}

// Метод для загрузки сцены
function loadScene(url, items){
	THREE.log("load scene", url);
	THREE.log("items", items);
	planItems = items;
	loader.load(url, sceneLoadedCallback);
}

function animate() {
	requestAnimationFrame(animate);
	controls.update();
}

function render() {
  pointLight.position.copy( camera.position );
  renderer.render(scene, camera);
}

function updateVisibility(obj){
	obj.visible = !obj.hideBlock && !obj.hideLevel && !obj.hideArticleType;
}

function toogleLevel(levelStr){
	var level = extractFloorNumber(levelStr);
	THREE.log("toogle level", level);
	var objects = levelMap[level];
	if(!objects){
		return;
	}
	for(var i = 0; i < objects.length; i++){
		var obj = objects[i];
		obj.hideLevel = !obj.hideLevel;
		updateVisibility(obj);
	}
	render();
}

function toogleBlock(blockStr){
	var block = extractBlock(blockStr);
	THREE.log("toogle block", block);
	var objects = blockMap[block];
	if(!objects){
		return;
	}
	for(var i = 0; i < objects.length; i++){
		var obj = objects[i];
		obj.hideBlock = !obj.hideBlock;
		updateVisibility(obj);
	}
	render();
}

function toogleArticleType(articleTypeStr){
	var articleType = extractArticleType(articleTypeStr);
	THREE.log("toogle article type", articleType);
	var objects = articleTypeMap[articleType];
	if(!objects){
		return;
	}
	for(var i = 0; i < objects.length; i++){
		var obj = objects[i];
		obj.hideArticleType = !obj.hideArticleType;
		updateVisibility(obj);
	}
	render();
}

function loadPlanItems(items){
	THREE.log("loadPlanItems", items);
	var item, itemObjs, itemObj;
	var plan_quantity, stock_quantity, done_quantity;
	
	for(var i = 0; i< items.length; i++){
		item = items[i];
		if(!item.name || !item.level || !item.block)
			continue;
		var key = item.name.toLowerCase() + ":" + extractFloorNumber(item.level) + ":" + extractBlock(item.block);
		
		itemObjs = itemsMap[key];
		if(!itemObjs)
			continue;
		plan_quantity = item.quantity - item.stockQuantity;
		stock_quantity = item.stockQuantity - item.localStockQuantity;
		done_quantity = item.localStockQuantity;
		
		for(var j = 0; j < itemObjs.length && (plan_quantity > 0 || stock_quantity >0 || done_quantity > 0); j++){
			itemObj = itemObjs[j];
			if(!!itemObj.status)
				continue;
			if(done_quantity > 0){
				done_quantity--;
				itemObj.status = "done";
				itemObj.children[0].material = status_materials.DONE;
				continue;
			}
			if(stock_quantity > 0){
				stock_quantity--;
				itemObj.status = "stock";
				itemObj.children[0].material = status_materials.STOCK;
				continue;
			}
			if(plan_quantity > 0){
				plan_quantity--;
				itemObj.status = "plan";
				itemObj.children[0].material = status_materials.PLAN;
				continue;
			}
			
		}
	}
	
	setDefaultStatus(status_materials.NEW);
	render();
}

