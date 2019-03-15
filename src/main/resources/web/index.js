var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var debounce = function debounce(callback, duration) {
  var timer;
  return function (event) {
    clearTimeout(timer);
    timer = setTimeout(function () {
      callback(event);
    }, duration);
  };
};

var loadTexs = function loadTexs(imgs, callback) {
  var texLoader = new THREE.TextureLoader();
  var length = Object.keys(imgs).length;
  var loadedTexs = {};
  var count = 0;

  texLoader.crossOrigin = 'anonymous';

  var _loop = function _loop() {
    var k = key;
    if (imgs.hasOwnProperty(k)) {
      texLoader.load(imgs[k], function (tex) {
        tex.repeat = THREE.RepeatWrapping;
        loadedTexs[k] = tex;
        count++;
        if (count >= length) callback(loadedTexs);
      });
    }
  };

  for (var key in imgs) {
    _loop();
  }
};

var Fog = function () {
  function Fog() {
    _classCallCheck(this, Fog);

    this.uniforms = {
      time: {
        type: 'f',
        value: 0
      },
      tex: {
        type: 't',
        value: null
      }
    };
    this.num = 200;
    this.obj = null;
  }

  _createClass(Fog, [{
    key: 'createObj',
    value: function createObj(tex) {
      // Define Geometries
      var geometry = new THREE.InstancedBufferGeometry();
      var baseGeometry = new THREE.PlaneBufferGeometry(1100, 1100, 20, 20);

      // Copy attributes of the base Geometry to the instancing Geometry
      geometry.addAttribute('position', baseGeometry.attributes.position);
      geometry.addAttribute('normal', baseGeometry.attributes.normal);
      geometry.addAttribute('uv', baseGeometry.attributes.uv);
      geometry.setIndex(baseGeometry.index);

      // Define attributes of the instancing geometry
      var instancePositions = new THREE.InstancedBufferAttribute(new Float32Array(this.num * 3), 3, 1);
      var delays = new THREE.InstancedBufferAttribute(new Float32Array(this.num), 1, 1);
      var rotates = new THREE.InstancedBufferAttribute(new Float32Array(this.num), 1, 1);
      for (var i = 0, ul = this.num; i < ul; i++) {
        instancePositions.setXYZ(i, (Math.random() * 2 - 1) * 850, 0, (Math.random() * 2 - 1) * 300);
        delays.setXYZ(i, Math.random());
        rotates.setXYZ(i, Math.random() * 2 + 1);
      }
      geometry.addAttribute('instancePosition', instancePositions);
      geometry.addAttribute('delay', delays);
      geometry.addAttribute('rotate', rotates);

      // Define Material
      var material = new THREE.RawShaderMaterial({
        uniforms: this.uniforms,
        vertexShader: '\n        attribute vec3 position;\n        attribute vec2 uv;\n        attribute vec3 instancePosition;\n        attribute float delay;\n        attribute float rotate;\n\n        uniform mat4 projectionMatrix;\n        uniform mat4 modelViewMatrix;\n        uniform float time;\n\n        varying vec3 vPosition;\n        varying vec2 vUv;\n        varying vec3 vColor;\n        varying float vBlink;\n\n        const float duration = 200.0;\n\n        mat4 calcRotateMat4Z(float radian) {\n          return mat4(\n            cos(radian), -sin(radian), 0.0, 0.0,\n            sin(radian), cos(radian), 0.0, 0.0,\n            0.0, 0.0, 1.0, 0.0,\n            0.0, 0.0, 0.0, 1.0\n          );\n        }\n        vec3 convertHsvToRgb(vec3 c) {\n          vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n          vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\n          return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n        }\n\n        void main(void) {\n          float now = mod(time + delay * duration, duration) / duration;\n\n          mat4 rotateMat = calcRotateMat4Z(radians(rotate * 360.0) + time * 0.1);\n          vec3 rotatePosition = (rotateMat * vec4(position, 1.0)).xyz;\n\n          vec3 moveRise = vec3(\n            (now * 2.0 - 1.0) * (2500.0 - (delay * 2.0 - 1.0) * 2000.0),\n            (now * 2.0 - 1.0) * 2000.0,\n            sin(radians(time * 50.0 + delay + length(position))) * 30.0\n            );\n          vec3 updatePosition = instancePosition + moveRise + rotatePosition;\n\n          vec3 hsv = vec3(time * 0.1 + delay * 0.2 + length(instancePosition) * 100.0, 0.5 , 0.8);\n          vec3 rgb = convertHsvToRgb(hsv);\n          float blink = (sin(radians(now * 360.0 * 20.0)) + 1.0) * 0.88;\n\n          vec4 mvPosition = modelViewMatrix * vec4(updatePosition, 1.0);\n\n          vPosition = position;\n          vUv = uv;\n          vColor = rgb;\n          vBlink = blink;\n\n          gl_Position = projectionMatrix * mvPosition;\n        }\n      ',
        fragmentShader: '\n        precision highp float;\n\n        uniform sampler2D tex;\n\n        varying vec3 vPosition;\n        varying vec2 vUv;\n        varying vec3 vColor;\n        varying float vBlink;\n\n        void main() {\n          vec2 p = vUv * 2.0 - 1.0;\n\n          vec4 texColor = texture2D(tex, vUv);\n          vec3 color = (texColor.rgb - vBlink * length(p) * 0.8) * vColor;\n          float opacity = texColor.a * 0.36;\n\n          gl_FragColor = vec4(color, opacity);\n        }\n      ',
        transparent: true,
        depthWrite: false,
        blending: THREE.AdditiveBlending
      });
      this.uniforms.tex.value = tex;

      // Create Object3D
      this.obj = new THREE.Mesh(geometry, material);
    }
  }, {
    key: 'render',
    value: function render(time) {
      this.uniforms.time.value += time;
    }
  }]);

  return Fog;
}();

var resolution = new THREE.Vector2();
var canvas = document.getElementById('canvas-webgl');
var renderer = new THREE.WebGLRenderer({
  alpha: true,
  antialias: true,
  canvas: canvas
});
var scene = new THREE.Scene();
var camera = new THREE.PerspectiveCamera();
var clock = new THREE.Clock();

camera.far = 50000;
camera.setFocalLength(24);

var texsSrc = {
  fog: 'https://raw.githubusercontent.com/VolmitSoftware/Fulcrum/master/images/fog.png'
};
var fog = new Fog();

var render = function render() {
  var time = clock.getDelta();
  fog.render(time);
  renderer.render(scene, camera);
};
var renderLoop = function renderLoop() {
  render();
  requestAnimationFrame(renderLoop);
};
var resizeCamera = function resizeCamera() {
  camera.aspect = resolution.x / resolution.y;
  camera.updateProjectionMatrix();
};
var resizeWindow = function resizeWindow() {
  resolution.set(window.innerWidth, window.innerHeight);
  canvas.width = resolution.x;
  canvas.height = resolution.y;
  resizeCamera();
  renderer.setSize(resolution.x, resolution.y);
};
var on = function on() {
  window.addEventListener('resize', debounce(resizeWindow), 1000);
};

var init = function init() {
  loadTexs(texsSrc, function (loadedTexs) {
    fog.createObj(loadedTexs.fog);

    scene.add(fog.obj);

    renderer.setClearColor(0x111111, 1.0);
    camera.position.set(0, 0, 1000);
    camera.lookAt(new THREE.Vector3());
    clock.start();

    on();
    resizeWindow();
    renderLoop();
  });
};
init();